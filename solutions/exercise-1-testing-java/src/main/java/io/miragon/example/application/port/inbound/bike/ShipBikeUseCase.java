package io.miragon.example.application.port.inbound.bike;

import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface ShipBikeUseCase {
    void shipBike(BikeSubscriptionId subscriptionId);
}
