package io.miragon.example.adapter.inbound.zeebe.bike;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.bike.ShipBikeUseCase;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShipBikeWorker {

    private static final Logger log = LoggerFactory.getLogger(ShipBikeWorker.class);

    private final ShipBikeUseCase useCase;

    public ShipBikeWorker(ShipBikeUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.BIKE_SHIP_BIKE)
    public void handle(@Variable String subscriptionId) {
        log.info("Shipping bike for subscription: {}", subscriptionId);
        useCase.shipBike(new BikeSubscriptionId(UUID.fromString(subscriptionId)));
    }

}
