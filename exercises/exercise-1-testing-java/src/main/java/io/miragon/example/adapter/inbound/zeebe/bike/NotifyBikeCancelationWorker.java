package io.miragon.example.adapter.inbound.zeebe.bike;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.bike.NotifyBikeCancelationUseCase;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotifyBikeCancelationWorker {

    private static final Logger log = LoggerFactory.getLogger(NotifyBikeCancelationWorker.class);

    private final NotifyBikeCancelationUseCase useCase;

    public NotifyBikeCancelationWorker(NotifyBikeCancelationUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.BIKE_NOTIFY_CANCELATION)
    public void handle(@Variable String subscriptionId) {
        log.info("Notifying about cancelation for subscription: {}", subscriptionId);
        useCase.notifyCancelation(new BikeSubscriptionId(UUID.fromString(subscriptionId)));
    }

}
