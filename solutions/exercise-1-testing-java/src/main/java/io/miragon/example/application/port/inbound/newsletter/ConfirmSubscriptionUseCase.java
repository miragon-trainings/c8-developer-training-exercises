package io.miragon.example.application.port.inbound.newsletter;

import io.miragon.example.domain.SubscriptionId;

public interface ConfirmSubscriptionUseCase {
    void confirm(SubscriptionId subscriptionId);
}
