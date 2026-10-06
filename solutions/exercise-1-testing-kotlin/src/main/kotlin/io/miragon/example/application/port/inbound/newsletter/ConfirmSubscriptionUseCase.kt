package io.miragon.example.application.port.inbound.newsletter

import io.miragon.example.domain.SubscriptionId

interface ConfirmSubscriptionUseCase {
    fun confirm(subscriptionId: SubscriptionId)
}