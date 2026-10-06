package io.miragon.example.adapter.outbound.zeebe.newsletter;

import io.miragon.example.adapter.process.TestProcessEngineConfiguration;
import io.miragon.example.application.port.inbound.newsletter.AbortSubscriptionUseCase;
import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase;
import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase;
import io.miragon.example.domain.SubscriptionId;
import io.camunda.process.test.api.CamundaAssert;
import io.camunda.process.test.api.CamundaProcessTestContext;
import io.camunda.process.test.api.CamundaSpringProcessTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.util.UUID;

import static io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi.FlowNodes.ActivityAbortRegistration;
import static io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi.FlowNodes.ActivitySendConfirmationMail;
import static io.camunda.process.test.api.assertions.ProcessInstanceSelectors.byKey;
import static org.mockito.Mockito.*;

/**
 * Process test for the Newsletter Subscription Process.
 * Uses native Camunda 8.8 test API with @CamundaSpringProcessTest and Spring Boot for component injection.
 * Workers are automatically registered via @JobWorker annotation.
 * Uses H2 an in-memory database for testing (configured in test/resources/application.yaml).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@CamundaSpringProcessTest
@Import(TestProcessEngineConfiguration.class)
class NewsletterSubscriptionProcessTest {

    @Autowired
    private CamundaProcessTestContext processTestContext;

    @Autowired
    private NewsletterSubscriptionProcessAdapter processPort;

    @MockitoBean
    private SendConfirmationMailUseCase sendConfirmationMailUseCase;

    @MockitoBean
    private SendWelcomeMailUseCase sendWelcomeMailUseCase;

    @MockitoBean
    private AbortSubscriptionUseCase abortSubscriptionUseCase;

    @BeforeEach
    void setup() {
        // No stubbing needed for void methods in Mockito
        // They can be called without setup
    }

    @Test
    void happyPathUserSubscribesToNewsletter() {
        // given
        UUID subscriptionId = UUID.fromString("4a607799-804b-43d1-8aa2-bdcc4dfd9b86");

        // when - start a process via undefined start event
        long instanceKey = processPort.submitForm(new SubscriptionId(subscriptionId));

        // then - process should be active
        CamundaAssert.assertThatProcessInstance(byKey(instanceKey)).isActive();

        // when - confirm subscription
        processPort.confirmSubscription(new SubscriptionId(subscriptionId));

        // Verify use cases were called
        CamundaAssert.assertThatProcessInstance(byKey(instanceKey)).isCompleted();
        verify(sendConfirmationMailUseCase).sendConfirmationMail(new SubscriptionId(subscriptionId));
        verify(sendWelcomeMailUseCase).sendWelcomeMail(new SubscriptionId(subscriptionId));
        verify(abortSubscriptionUseCase, never()).abort(any());
        verifyNoMoreInteractions(sendConfirmationMailUseCase, sendWelcomeMailUseCase, abortSubscriptionUseCase);
    }

    @Test
    void abortRegistrationIfUserHasNotConfirmedAfter3Minutes() {
        // given
        UUID subscriptionId = UUID.fromString("4a607799-804b-43d1-8aa2-bdcc4dfd9b87");

        // when - start process via message
        long instanceKey = processPort.submitForm(new SubscriptionId(subscriptionId));

        // then - let 3 minutes pass to send mails
        processTestContext.increaseTime(Duration.ofSeconds(60));
        CamundaAssert
            .assertThatProcessInstance(byKey(instanceKey))
            .hasCompletedElement(ActivitySendConfirmationMail.ELEMENT_ID,1);

        processTestContext.increaseTime(Duration.ofSeconds(60));
        CamundaAssert.assertThatProcessInstance(byKey(instanceKey))
            .hasCompletedElement(ActivitySendConfirmationMail.ELEMENT_ID,2);

        processTestContext.increaseTime(Duration.ofSeconds(30));
        CamundaAssert.assertThatProcessInstance(byKey(instanceKey))
            .hasCompletedElement(ActivityAbortRegistration.ELEMENT_ID, 1);

        // then - process should abort
        CamundaAssert.assertThatProcessInstance(byKey(instanceKey)).isCompleted();
        verify(sendConfirmationMailUseCase, times(2)).sendConfirmationMail(new SubscriptionId(subscriptionId));
        verify(abortSubscriptionUseCase).abort(new SubscriptionId(subscriptionId));
        verify(sendWelcomeMailUseCase, never()).sendWelcomeMail(any());
        verifyNoMoreInteractions(sendConfirmationMailUseCase, sendWelcomeMailUseCase, abortSubscriptionUseCase);
    }
}
