package io.miragon.example.application.port.inbound.newsletter

import io.miragon.example.domain.SubscriptionId

interface AbortSubscriptionUseCase {
    fun abort(subscriptionId: SubscriptionId)
}