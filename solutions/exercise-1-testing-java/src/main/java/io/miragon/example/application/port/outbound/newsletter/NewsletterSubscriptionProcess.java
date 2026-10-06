package io.miragon.example.application.port.outbound.newsletter;

import io.miragon.example.domain.SubscriptionId;

public interface NewsletterSubscriptionProcess {
    long submitForm(SubscriptionId id);
    void confirmSubscription(SubscriptionId id);
}
