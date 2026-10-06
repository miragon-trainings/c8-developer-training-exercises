package io.miragon.example.adapter.in.zeebe;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.in.IncrementSubscriptionCounterUseCase;
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
public class RegistrationCompletedWorker {

    private static final Logger log = LoggerFactory.getLogger(RegistrationCompletedWorker.class);

    private final IncrementSubscriptionCounterUseCase useCase;

    public RegistrationCompletedWorker(IncrementSubscriptionCounterUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.NEWSLETTER_REGISTRATION_COMPLETED)
    public void handleRegistrationCompleted(
            ActivatedJob job,
            @Variable("subscriptionId") String subscriptionId
    ) {
        log.debug("Received Zeebe job for registration completed: {}", subscriptionId);
        useCase.incrementCounter(
                new SubscriptionId(UUID.fromString(subscriptionId)),
                new OperationId(subscriptionId + "-" + job.getElementId())
        );
    }
}
