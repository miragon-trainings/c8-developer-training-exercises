package io.miragon.example.adapter.process

import com.ninjasquad.springmockk.MockkBean
import io.camunda.client.CamundaClient
import io.camunda.client.api.response.ProcessInstanceEvent
import io.camunda.process.test.api.CamundaAssert
import io.camunda.process.test.api.CamundaProcessTestContext
import io.camunda.process.test.api.CamundaSpringProcessTest
import io.camunda.process.test.api.assertions.ProcessInstanceSelectors
import io.miragon.example.adapter.outbound.zeebe.newsletter.NewsletterSubscriptionProcessAdapter
import io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi
import io.miragon.bpmn.runtime.ElementId
import io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi.FlowNodes
import io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi.FlowNodes.StartEventSubmitRegistrationForm.Variables.SUBSCRIPTION_ID
import io.miragon.example.application.port.inbound.newsletter.AbortSubscriptionUseCase
import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase
import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase
import io.miragon.example.domain.SubscriptionId
import io.mockk.Called
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.time.Duration
import java.util.*

/**
 * Process test for the Newsletter Subscription Process.
 * Uses native Camunda 8.8 test API with @CamundaSpringProcessTest and Spring Boot for component injection.
 * Workers are automatically registered via @JobWorker annotation.
 * Uses H2 an in-memory database for testing (configured in test/resources/application.yaml).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@CamundaSpringProcessTest
@Import(TestProcessEngineConfiguration::class)
class NewsletterSubscriptionProcessTest {

    @Autowired
    private lateinit var camundaClient: CamundaClient

    @Autowired
    private lateinit var processTestContext: CamundaProcessTestContext

    @Autowired
    private lateinit var processPort: NewsletterSubscriptionProcessAdapter

    @MockkBean
    private lateinit var sendConfirmationMailUseCase: SendConfirmationMailUseCase

    @MockkBean
    private lateinit var sendWelcomeMailUseCase: SendWelcomeMailUseCase

    @MockkBean
    private lateinit var abortSubscriptionUseCase: AbortSubscriptionUseCase

    @BeforeEach
    fun setup() {
        every { sendConfirmationMailUseCase.sendConfirmationMail(any<SubscriptionId>()) } just Runs
        every { sendWelcomeMailUseCase.sendWelcomeMail(any<SubscriptionId>()) } just Runs
        every { abortSubscriptionUseCase.abort(any<SubscriptionId>()) } just Runs
    }

    @Test
    fun `happy path - user confirms subscription immediately and receives welcome mail`() {

        // given
        val subscriptionId = UUID.fromString("4a607799-804b-43d1-8aa2-bdcc4dfd9b86")

        // when - start process and immediately confirm subscription
        val instanceKey = processPort.submitForm(SubscriptionId(subscriptionId))
        processPort.confirmSubscription(SubscriptionId(subscriptionId))

        // then - process should complete successfully with welcome mail sent
        val instance = ProcessInstanceSelectors.byKey(instanceKey)
        CamundaAssert.assertThatProcessInstance(instance).isCompleted()
        verify { sendConfirmationMailUseCase.sendConfirmationMail(SubscriptionId(subscriptionId)) }
        verify { sendWelcomeMailUseCase.sendWelcomeMail(SubscriptionId(subscriptionId)) }
        verify { abortSubscriptionUseCase wasNot Called }
        confirmVerified(sendConfirmationMailUseCase, sendWelcomeMailUseCase, abortSubscriptionUseCase)
    }

    @Test
    fun `user confirms subscription after receiving reminder mail`() {

        // given - process is started
        val subscriptionId = UUID.fromString("4a607799-804b-43d1-8aa2-bdcc4dfd9b87")
        val instance = startProcessAt(
            elementId = FlowNodes.ActivitySendConfirmationMail.id,
            subscriptionId = SubscriptionId(subscriptionId)
        )

        // when - time passes, and reminder is sent; then the user confirms
        CamundaAssert.assertThat(instance).hasCompletedElement(FlowNodes.ActivitySendConfirmationMail.ELEMENT_ID, 1)
        processTestContext.increaseTime(Duration.ofSeconds(60))
        CamundaAssert.assertThatProcessInstance(instance)
            .hasCompletedElement(FlowNodes.ActivitySendConfirmationMail.ELEMENT_ID, 2)

        processPort.confirmSubscription(SubscriptionId(subscriptionId))

        // then - process completes successfully
        CamundaAssert.assertThatProcessInstance(instance).isCompleted()
        verify(exactly = 2) { sendConfirmationMailUseCase.sendConfirmationMail(SubscriptionId(subscriptionId)) }
        verify { sendWelcomeMailUseCase.sendWelcomeMail(SubscriptionId(subscriptionId)) }
        verify { abortSubscriptionUseCase wasNot Called }
        confirmVerified(sendConfirmationMailUseCase, sendWelcomeMailUseCase, abortSubscriptionUseCase)
    }

    @Test
    fun `subscription is aborted when user does not confirm within timeout`() {

        // given - process is started
        val subscriptionId = UUID.fromString("4a607799-804b-43d1-8aa2-bdcc4dfd9b88")
        val instance = startProcessAt(
            elementId = FlowNodes.ActivityConfirmRegistration.id,
            subscriptionId = SubscriptionId(subscriptionId)
        )

        // when - timeout period (2.5 minutes) passes without confirmation
        processTestContext.increaseTime(Duration.ofSeconds(150))

        // then - subscription is aborted
        CamundaAssert.assertThatProcessInstance(instance)
            .hasCompletedElement(FlowNodes.ActivityAbortRegistration.ELEMENT_ID, 1)
        CamundaAssert.assertThatProcessInstance(instance).isCompleted

        verify { abortSubscriptionUseCase.abort(SubscriptionId(subscriptionId)) }
        verify { sendWelcomeMailUseCase wasNot Called }
        confirmVerified(sendConfirmationMailUseCase, sendWelcomeMailUseCase, abortSubscriptionUseCase)
    }

    private fun startProcessAt(
        elementId: ElementId,
        subscriptionId: SubscriptionId
    ): ProcessInstanceEvent {
        val variables = mapOf(SUBSCRIPTION_ID.value to subscriptionId.value.toString())
        return camundaClient.newCreateInstanceCommand()
            .bpmnProcessId(NewsletterSubscriptionProcessApi.PROCESS_ID.value)
            .latestVersion()
            .variables(variables)
            .startBeforeElement(elementId.value)
            .send()
            .join()
    }
}