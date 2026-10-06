package io.miragon.example.application.port.inbound.bike;

import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface NotifyBikeCancelationUseCase {
    void notifyCancelation(BikeSubscriptionId subscriptionId);
}
