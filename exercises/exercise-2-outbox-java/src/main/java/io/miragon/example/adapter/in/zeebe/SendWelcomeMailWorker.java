package io.miragon.example.adapter.in.zeebe;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.in.SendWelcomeMailUseCase;
import io.miragon.example.domain.OperationId;
import io.miragon.example.domain.SubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import io.camunda.client.api.response.ActivatedJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendWelcomeMailWorker {

    private static final Logger log = LoggerFactory.getLogger(SendWelcomeMailWorker.class);

    private final SendWelcomeMailUseCase useCase;

    public SendWelcomeMailWorker(SendWelcomeMailUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.NEWSLETTER_SEND_WELCOME_MAIL)
    public void sendWelcomeMail(
            ActivatedJob job,
            @Variable("subscriptionId") String subscriptionId
    ) {
        log.debug("Received Zeebe job to send welcome mail: {}", subscriptionId);
        useCase.sendWelcomeMail(
                new SubscriptionId(UUID.fromString(subscriptionId)),
                new OperationId(subscriptionId + "-" + job.getElementId())
        );
    }
}
