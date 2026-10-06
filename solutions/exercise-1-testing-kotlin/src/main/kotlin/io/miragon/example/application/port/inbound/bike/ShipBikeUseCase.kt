package io.miragon.example.application.port.inbound.bike

import io.miragon.example.domain.bike.BikeSubscriptionId

interface ShipBikeUseCase {
    fun shipBike(subscriptionId: BikeSubscriptionId)
}
