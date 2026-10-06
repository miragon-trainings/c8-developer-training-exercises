package io.miragon.example.application.port.inbound.newsletter

import io.miragon.example.domain.SubscriptionId

interface SendWelcomeMailUseCase {
    fun sendWelcomeMail(subscriptionId: SubscriptionId)
}