package io.miragon.example.adapter.outbound.zeebe.bike;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import io.camunda.process.test.api.CamundaAssert;
import io.camunda.process.test.api.CamundaProcessTestContext;
import io.camunda.process.test.api.CamundaSpringProcessTest;
import io.miragon.example.adapter.process.TestProcessEngineConfiguration;
import io.miragon.example.adapter.process.generated.BikeSubscriptionSignupProcessApi;
import io.miragon.example.adapter.process.generated.BikeSubscriptionSignupProcessApi.FlowNodes;
import io.miragon.example.adapter.process.generated.Messages;
import io.miragon.example.adapter.process.generated.ProcessVariables;
import io.miragon.example.application.port.inbound.bike.CheckBikeAvailabilityUseCase;
import io.miragon.example.application.port.inbound.bike.NotifyBikeCancelationUseCase;
import io.miragon.example.application.port.inbound.bike.SendBikeConfirmationMailUseCase;
import io.miragon.example.application.port.inbound.bike.SendBikeWelcomeMailUseCase;
import io.miragon.example.application.port.inbound.bike.SendPaymentReminderUseCase;
import io.miragon.example.application.port.inbound.bike.SendRejectionMailUseCase;
import io.miragon.example.application.port.inbound.bike.ShipBikeUseCase;
import io.miragon.example.domain.bike.BikeId;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import static io.camunda.process.test.api.assertions.ProcessInstanceSelectors.byKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Reference solution of the process test for the Bike Subscription Process.
 * <p>
 * Uses native Camunda 8.8 test API with @CamundaSpringProcessTest and Spring Boot for component injection.
 * Workers are automatically registered via @JobWorker annotation.
 * Uses H2 an in-memory database for testing (configured in test/resources/application.yaml).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@CamundaSpringProcessTest
@Import(TestProcessEngineConfiguration.class)
class BikeSubscriptionProcessTest {
	
	@Autowired
	private CamundaClient camundaClient;
	
	@Autowired
	private CamundaProcessTestContext processTestContext;
	
	@Autowired
	private BikeSubscriptionProcessAdapter processPort;
	
	@MockitoBean
	private CheckBikeAvailabilityUseCase checkAvailabilityUseCase;
	
	@MockitoBean
	private SendRejectionMailUseCase sendRejectionMailUseCase;
	
	@MockitoBean
	private SendBikeConfirmationMailUseCase sendConfirmationMailUseCase;
	
	@MockitoBean
	private SendPaymentReminderUseCase sendPaymentReminderUseCase;
	
	@MockitoBean
	private NotifyBikeCancelationUseCase notifyCancelationUseCase;
	
	@MockitoBean
	private ShipBikeUseCase shipBikeUseCase;
	
	@MockitoBean
	private SendBikeWelcomeMailUseCase sendWelcomeMailUseCase;
	
	@BeforeEach
	void setup() {
		when(checkAvailabilityUseCase.checkAvailability(any())).thenReturn(true);
	}
	
	@AfterEach
	void confirmCalls() {
		verifyNoMoreInteractions(
				checkAvailabilityUseCase,
				sendRejectionMailUseCase,
				sendConfirmationMailUseCase,
				sendPaymentReminderUseCase,
				notifyCancelationUseCase,
				shipBikeUseCase,
				sendWelcomeMailUseCase
		);
	}
	
	@Test
	void happyPath() {
		
		// given: running process
		BikeSubscriptionId subscriptionId = new BikeSubscriptionId(UUID.randomUUID());
		long instanceKey = processPort.startSubscription(subscriptionId, new BikeId(UUID.randomUUID()));
		var instance = byKey(instanceKey);
		
		// when: payment is received
		CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.PAYMENT_RECEIVED.getValue());
		processPort.sendPaymentReceived(subscriptionId);
		
		// when: bike is received
		CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.BIKE_RECEIVED.getValue());
		processPort.sendBikeReceived(subscriptionId);
		
		// then: process should complete successfully
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
						FlowNodes.EndEventSubscriptionActive.ELEMENT_ID
				);
		
		verify(checkAvailabilityUseCase).checkAvailability(subscriptionId);
		verify(sendConfirmationMailUseCase).sendConfirmationMail(subscriptionId);
		verify(shipBikeUseCase).shipBike(subscriptionId);
		verify(sendWelcomeMailUseCase).sendWelcomeMail(subscriptionId);
	}
	
	@Test
	void informCustomerThatBikeIsNotAvailable() {
		
		// given: bike is not available
		BikeSubscriptionId subscriptionId = new BikeSubscriptionId(UUID.randomUUID());
		when(checkAvailabilityUseCase.checkAvailability(any())).thenReturn(false);
		
		// when: subscription is started
		long instanceKey = processPort.startSubscription(subscriptionId, new BikeId(UUID.randomUUID()));
		var instance = byKey(instanceKey);
		
		// then: process should complete with rejection path
		CamundaAssert.assertThat(instance)
				.isCompleted()
				.hasCompletedElements(
						FlowNodes.StartEventSubscriptionRequested.ELEMENT_ID,
						FlowNodes.ActivityCheckAvailability.ELEMENT_ID,
						FlowNodes.ActivitySendRejectionMail.ELEMENT_ID,
						FlowNodes.EndEventOfferNotPossible.ELEMENT_ID
				);
		
		verify(checkAvailabilityUseCase).checkAvailability(subscriptionId);
		verify(sendRejectionMailUseCase).sendRejectionMail(subscriptionId);
	}
	
	@Test
	void informCustomerAboutPayment() {
		
		// given: instance that is waiting for payment
		BikeSubscriptionId subscriptionId = new BikeSubscriptionId(UUID.randomUUID());
		var instance = startProcessAt(FlowNodes.ActivityWaitForPayment.ELEMENT_ID, subscriptionId);
		CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.PAYMENT_RECEIVED.getValue());
		
		// when: three days are passing
		processTestContext.increaseTime(Duration.ofDays(3));
		
		// then: a reminder should be sent
		CamundaAssert.assertThat(instance)
				.isWaitingForMessage(Messages.PAYMENT_RECEIVED.getValue())
				.hasCompletedElements(
						FlowNodes.TimerEvery3Days.ELEMENT_ID,
						FlowNodes.ActivitySendPaymentReminder.ELEMENT_ID,
						FlowNodes.EndEventCustomerReminded.ELEMENT_ID
				);
		
		verify(sendPaymentReminderUseCase).sendPaymentReminder(subscriptionId);
	}
	
	@Test
	void informCustomerAboutCancelation() {
		
		// given: instance that is waiting for payment
		BikeSubscriptionId subscriptionId = new BikeSubscriptionId(UUID.randomUUID());
		var instance = startProcessAt(FlowNodes.ActivityWaitForPayment.ELEMENT_ID, subscriptionId);
		CamundaAssert.assertThat(instance).isWaitingForMessage(Messages.PAYMENT_RECEIVED.getValue());
		
		// when: customer aborts the subscription request
		processPort.sendRequestCanceled(subscriptionId);
		
		// then: abortion processed successfully
		CamundaAssert.assertThat(instance)
				.isCompleted()
				.hasTerminatedElement(FlowNodes.ActivityWaitForPayment.ELEMENT_ID, 1)
				.hasCompletedElements(
						FlowNodes.MessageRequestCanceledEvent.ELEMENT_ID,
						FlowNodes.ActivityNotifyAboutCancelation.ELEMENT_ID,
						FlowNodes.EndEventRequestCanceled.ELEMENT_ID
				);
		
		verify(notifyCancelationUseCase).notifyCancelation(subscriptionId);
	}
	
	private ProcessInstanceEvent startProcessAt(String elementId, BikeSubscriptionId subscriptionId) {
		Map<String, Object> variables = Map.of(ProcessVariables.SUBSCRIPTION_ID, subscriptionId.value().toString());
		return camundaClient.newCreateInstanceCommand()
				.bpmnProcessId(BikeSubscriptionSignupProcessApi.PROCESS_ID.getValue())
				.latestVersion()
				.variables(variables)
				.startBeforeElement(elementId)
				.send()
				.join();
	}
}
