package io.miragon.example.adapter.in.zeebe;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.in.AbortSubscriptionUseCase;
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
public class AbortSubscriptionWorker {

    private static final Logger log = LoggerFactory.getLogger(AbortSubscriptionWorker.class);

    private final AbortSubscriptionUseCase useCase;

    public AbortSubscriptionWorker(AbortSubscriptionUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.NEWSLETTER_ABORT_REGISTRATION)
    public void abortRegistration(
            ActivatedJob job,
            @Variable("subscriptionId") String subscriptionId
    ) {
        log.debug("Received Zeebe job to abort subscription: {}", subscriptionId);
        useCase.abort(
                new SubscriptionId(UUID.fromString(subscriptionId)),
                new OperationId(subscriptionId + "-" + job.getElementId())
        );
    }
}
