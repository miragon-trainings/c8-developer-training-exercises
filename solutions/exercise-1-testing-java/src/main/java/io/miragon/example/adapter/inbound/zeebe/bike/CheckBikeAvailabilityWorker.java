package io.miragon.example.adapter.inbound.zeebe.bike;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.bike.CheckBikeAvailabilityUseCase;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class CheckBikeAvailabilityWorker {

    private static final Logger log = LoggerFactory.getLogger(CheckBikeAvailabilityWorker.class);

    private final CheckBikeAvailabilityUseCase useCase;

    public CheckBikeAvailabilityWorker(CheckBikeAvailabilityUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.BIKE_CHECK_AVAILABILITY)
    public Map<String, Object> handle(@Variable String subscriptionId) {
        log.info("Checking bike availability for subscription: {}", subscriptionId);
        boolean available = useCase.checkAvailability(new BikeSubscriptionId(UUID.fromString(subscriptionId)));
        return Map.of("bikeAvailable", available);
    }

}
