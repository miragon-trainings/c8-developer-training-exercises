package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.ShipBikeUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ShipBikeService(
    private val repository: BikeSubscriptionRepository
) : ShipBikeUseCase {

    private val log = KotlinLogging.logger {}

    override fun shipBike(subscriptionId: BikeSubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Shipping bike ${subscription.bikeId.value} to customer ${subscription.customerName.value}" }
        repository.save(subscription.markShipped())
    }
}
