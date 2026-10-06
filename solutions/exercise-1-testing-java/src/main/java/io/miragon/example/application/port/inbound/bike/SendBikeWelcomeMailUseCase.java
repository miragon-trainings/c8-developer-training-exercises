package io.miragon.example.application.port.inbound.bike;

import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface SendBikeWelcomeMailUseCase {
    void sendWelcomeMail(BikeSubscriptionId subscriptionId);
}
