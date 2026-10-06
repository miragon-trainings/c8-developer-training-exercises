package io.miragon.example.application.port.inbound.bike

import io.miragon.example.domain.Email
import io.miragon.example.domain.Name
import io.miragon.example.domain.bike.BikeId
import io.miragon.example.domain.bike.BikeSubscriptionId

interface StartBikeSubscriptionUseCase {
    data class Command(
        val bikeId: BikeId,
        val email: Email,
        val name: Name
    )

    fun start(command: Command): BikeSubscriptionId
}
