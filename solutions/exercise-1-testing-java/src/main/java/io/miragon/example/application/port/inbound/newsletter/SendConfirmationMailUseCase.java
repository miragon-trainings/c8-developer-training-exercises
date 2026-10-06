package io.miragon.example.application.port.inbound.newsletter;

import io.miragon.example.domain.SubscriptionId;

public interface SendConfirmationMailUseCase {
    void sendConfirmationMail(SubscriptionId subscriptionId);
}
