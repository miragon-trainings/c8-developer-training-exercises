package io.miragon.example.application.port.outbound.newsletter;

import io.miragon.example.domain.NewsletterSubscription;
import io.miragon.example.domain.SubscriptionId;

public interface NewsletterSubscriptionRepository {
    NewsletterSubscription find(SubscriptionId subscriptionId);
    NewsletterSubscription search(SubscriptionId subscriptionId);
    void save(NewsletterSubscription subscription);
    void delete(SubscriptionId subscriptionId);
}
