package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.CheckBikeAvailabilityUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class CheckBikeAvailabilityService(
    private val repository: BikeSubscriptionRepository
) : CheckBikeAvailabilityUseCase {

    private val log = KotlinLogging.logger {}

    override fun checkAvailability(subscriptionId: BikeSubscriptionId): Boolean {
        val subscription = repository.find(subscriptionId)

        // Mock availability check: 80% chance of being available
        val available = Random.nextDouble() < 0.8

        log.info { "Bike ${subscription.bikeId.value} availability check: $available" }

        repository.save(subscription.markAvailability(available))

        return available
    }
}
