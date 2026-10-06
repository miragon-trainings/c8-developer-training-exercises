package io.miragon.example.application.port.outbound.newsletter

import io.miragon.example.domain.SubscriptionId

interface NewsletterSubscriptionProcess {
    fun submitForm(id: SubscriptionId): Long
    fun confirmSubscription(id: SubscriptionId)
}