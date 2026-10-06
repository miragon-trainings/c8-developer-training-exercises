package io.miragon.example.adapter.process.bike

import com.ninjasquad.springmockk.MockkBean
import io.camunda.client.CamundaClient
import io.camunda.client.api.response.ProcessInstanceEvent
import io.camunda.process.test.api.CamundaAssert
import io.camunda.process.test.api.CamundaProcessTestContext
import io.camunda.process.test.api.CamundaSpringProcessTest
import io.camunda.process.test.api.assertions.ProcessInstanceSelectors
import io.miragon.example.adapter.outbound.zeebe.bike.BikeSubscriptionProcessAdapter
import io.miragon.example.adapter.process.TestProcessEngineConfiguration
import io.miragon.example.adapter.process.generated.BikeSubscriptionSignupProcessApi
import io.miragon.example.adapter.process.generated.BikeSubscriptionSignupProcessApi.FlowNodes
import io.miragon.example.adapter.process.generated.Messages
import io.miragon.example.adapter.process.generated.ProcessVariables
import io.miragon.example.application.port.inbound.bike.CheckBikeAvailabilityUseCase
import io.miragon.example.application.port.inbound.bike.NotifyBikeCancelationUseCase
import io.miragon.example.application.port.inbound.bike.SendBikeConfirmationMailUseCase
import io.miragon.example.application.port.inbound.bike.SendBikeWelcomeMailUseCase
import io.miragon.example.application.port.inbound.bike.SendPaymentReminderUseCase
import io.miragon.example.application.port.inbound.bike.SendRejectionMailUseCase
import io.miragon.example.application.port.inbound.bike.ShipBikeUseCase
import io.miragon.example.domain.bike.BikeId
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.time.Duration
import java.util.UUID

/**
 * Reference solution of the process test for the Bike Subscription Process.
 *
 * Uses native Camunda 8.8 test API with @CamundaSpringProcessTest and Spring Boot for component injection.
 * Workers are automatically registered via @JobWorker annotation.
 * Uses H2 an in-memory database for testing (configured in test/resources/application.yaml).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@CamundaSpringProcessTest
@Import(TestProcessEngineConfiguration::class)
class BikeSubscriptionProcessTest {

    @Autowired
    private lateinit var camundaClient: CamundaClient

    @Autowired
    private lateinit var processTestContext: CamundaProcessTestContext

    @Autowired
    private lateinit var processPort: BikeSubscriptionProcessAdapter

    @MockkBean
    private lateinit var checkAvailabilityUseCase: CheckBikeAvailabilityUseCase

    @MockkBean
    private lateinit var sendRejectionMailUseCase: SendRejectionMailUseCase

    @MockkBean
    private lateinit var sendConfirmationMailUseCase: SendBikeConfirmationMailUseCase

    @MockkBean
    private lateinit var sendPaymentReminderUseCase: SendPaymentReminderUseCase

    @MockkBean
    private lateinit var notifyCancelationUseCase: NotifyBikeCancelationUseCase

    @MockkBean
    private lateinit var shipBikeUseCase: ShipBikeUseCase

    @MockkBean
    private lateinit var sendWelcomeMailUseCase: SendBikeWelcomeMailUseCase

    @BeforeEach
    fun setup() {
        every { checkAvailabilityUseCase.checkAvailability(any()) } returns true
        every { sendRejectionMailUseCase.sendRejectionMail(any()) } just Runs
        every { sendConfirmationMailUseCase.sendConfirmationMail(any()) } just Runs
        every { sendPaymentReminderUseCase.sendPaymentReminder(any()) } just Runs
        every { notifyCancelationUseCase.notifyCancelation(any()) } just Runs
        every { shipBikeUseCase.shipBike(any()) } just Runs
        every { sendWelcomeMailUseCase.sendWelcomeMail(any()) } just Runs
    }

    @AfterEach
    fun confirmCalls() = confirmVerified(
        checkAvailabilityUseCase, sendRejectionMailUseCase,
        sendConfirmationMailUseCase, sendPaymentReminderUseCase,
        notifyCancelationUseCase, shipBikeUseCase, sendWelcomeMailUseCase
    )

    @Test
    fun `happy path`() {

        // given: active instance
        val subscriptionId = BikeSubscriptionId(UUID.randomUUID())
        val instanceKey = processPort.startSubscription(id = subscriptionId, bikeId = BikeId(UUID.randomUUID()))
        val instance = ProcessInstanceSelectors.byKey(instanceKey)

        // when: payment is received
        CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.PAYMENT_RECEIVED.value)
        processPort.sendPaymentReceived(subscriptionId)

        // when: bike is received
        CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.BIKE_RECEIVED.value)
        processPort.sendBikeReceived(subscriptionId)

        // then: process completes successfully
        CamundaAssert.assertThat(instance)
            .isCompleted()
            .hasCompletedElements(
                FlowNodes.StartEventSubscriptionRequested.ELEMENT_ID,
                FlowNodes.ActivityCheckAvailability.ELEMENT_ID,
                FlowNodes.GatewayBikeAvailable.ELEMENT_ID,
                FlowNodes.ActivitySendConfirmationMail.ELEMENT_ID,
                FlowNodes.ActivityWaitForPayment.ELEMENT_ID,
                FlowNodes.ActivityShipBike.ELEMENT_ID,
                FlowNodes.ActivityWaitForDelivery.ELEMENT_ID,
                FlowNodes.ActivitySendWelcomeMail.ELEMENT_ID,
                FlowNodes.EndEventSubscriptionActive.ELEMENT_ID,
            )

        verify { checkAvailabilityUseCase.checkAvailability(subscriptionId) }
        verify { sendConfirmationMailUseCase.sendConfirmationMail(subscriptionId) }
        verify { shipBikeUseCase.shipBike(subscriptionId) }
        verify { sendWelcomeMailUseCase.sendWelcomeMail(subscriptionId) }
    }

    @Test
    fun `inform customer that bike is not available`() {

        // given: the bike is not available
        val subscriptionId = BikeSubscriptionId(UUID.randomUUID())
        every { checkAvailabilityUseCase.checkAvailability(any()) } returns false

        // when: subscription is started
        val instanceKey = processPort.startSubscription(id = subscriptionId, bikeId = BikeId(UUID.randomUUID()))
        val instance = ProcessInstanceSelectors.byKey(instanceKey)

        // then: process completes with rejection path
        CamundaAssert.assertThat(instance)
            .isCompleted()
            .hasCompletedElements(
                FlowNodes.StartEventSubscriptionRequested.ELEMENT_ID,
                FlowNodes.ActivityCheckAvailability.ELEMENT_ID,
                FlowNodes.ActivitySendRejectionMail.ELEMENT_ID,
                FlowNodes.EndEventOfferNotPossible.ELEMENT_ID,
            )

        verify { checkAvailabilityUseCase.checkAvailability(subscriptionId) }
        verify { sendRejectionMailUseCase.sendRejectionMail(subscriptionId) }
    }

    @Test
    fun `inform customer about payment`() {

        // given: instance that is waiting for payment
        val subscriptionId = BikeSubscriptionId(UUID.randomUUID())
        val instance = startProcessAt(FlowNodes.ActivityWaitForPayment.ELEMENT_ID, subscriptionId)
        CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.PAYMENT_RECEIVED.value)

        // when: three days are passing
        processTestContext.increaseTime(Duration.ofDays(3))

        // then: reminder should be sent
        CamundaAssert.assertThat(instance)
            .isWaitingForMessage(Messages.PAYMENT_RECEIVED.value)
            .hasCompletedElements(
                FlowNodes.TimerEvery3Days.ELEMENT_ID,
                FlowNodes.ActivitySendPaymentReminder.ELEMENT_ID,
                FlowNodes.EndEventCustomerReminded.ELEMENT_ID,
            )

        verify { sendPaymentReminderUseCase.sendPaymentReminder(subscriptionId) }
    }

    @Test
    fun `inform customer about cancelation`() {

        // given: instance that is waiting for payment
        val subscriptionId = BikeSubscriptionId(UUID.randomUUID())
        val instance = startProcessAt(FlowNodes.ActivityWaitForPayment.ELEMENT_ID, subscriptionId)
        CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.PAYMENT_RECEIVED.value)

        // when: customer aborts the subscription request
        processPort.sendRequestCanceled(subscriptionId)

        // then: abortion processed successfully
        CamundaAssert.assertThat(instance)
            .isCompleted()
            .hasTerminatedElement(FlowNodes.ActivityWaitForPayment.ELEMENT_ID, 1)
            .hasCompletedElements(
                FlowNodes.MessageRequestCanceledEvent.ELEMENT_ID,
                FlowNodes.ActivityNotifyAboutCancelation.ELEMENT_ID,
                FlowNodes.EndEventRequestCanceled.ELEMENT_ID,
            )

        verify { notifyCancelationUseCase.notifyCancelation(subscriptionId) }
    }

    private fun startProcessAt(
        elementId: String,
        subscriptionId: BikeSubscriptionId
    ): ProcessInstanceEvent = camundaClient.newCreateInstanceCommand()
        .bpmnProcessId(BikeSubscriptionSignupProcessApi.PROCESS_ID.value)
        .latestVersion()
        .variables(mapOf(ProcessVariables.SUBSCRIPTION_ID to subscriptionId.value.toString()))
        .startBeforeElement(elementId)
        .send()
        .join()
}
