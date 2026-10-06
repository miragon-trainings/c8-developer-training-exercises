package io.miragon.example.adapter.in.zeebe;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.in.SendConfirmationMailUseCase;
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
public class SendConfirmationMailWorker {

    private static final Logger log = LoggerFactory.getLogger(SendConfirmationMailWorker.class);

    private final SendConfirmationMailUseCase useCase;

    public SendConfirmationMailWorker(SendConfirmationMailUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.NEWSLETTER_SEND_CONFIRMATION_MAIL)
    public void sendConfirmationMail(
            ActivatedJob job,
            @Variable("subscriptionId") String subscriptionId
    ) {
        log.debug("Received Zeebe job to send confirmation mail: {}", subscriptionId);
        useCase.sendConfirmationMail(
                new SubscriptionId(UUID.fromString(subscriptionId)),
                new OperationId(subscriptionId + "-" + job.getElementId())
        );
    }
}
