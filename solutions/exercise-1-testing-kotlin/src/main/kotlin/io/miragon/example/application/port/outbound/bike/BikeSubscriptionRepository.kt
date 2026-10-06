package io.miragon.example.application.port.outbound.bike

import io.miragon.example.domain.bike.BikeSubscription
import io.miragon.example.domain.bike.BikeSubscriptionId

interface BikeSubscriptionRepository {
    fun save(subscription: BikeSubscription): BikeSubscription
    fun find(id: BikeSubscriptionId): BikeSubscription
    fun search(id: BikeSubscriptionId): BikeSubscription?
}
