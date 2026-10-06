package io.miragon.example.application.port.outbound.newsletter

import io.miragon.example.domain.newsletter.NewsletterSubscription
import io.miragon.example.domain.SubscriptionId

interface NewsletterSubscriptionRepository {
    fun find(subscriptionId: SubscriptionId): NewsletterSubscription
    fun search(subscriptionId: SubscriptionId): NewsletterSubscription?
    fun save(subscription: NewsletterSubscription)
    fun delete(subscriptionId: SubscriptionId)
}