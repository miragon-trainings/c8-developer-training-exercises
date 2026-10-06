package io.miragon.example.application.port.inbound.bike;

import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface SendRejectionMailUseCase {
    void sendRejectionMail(BikeSubscriptionId subscriptionId);
}
