package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.StartBikeSubscriptionUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionProcess
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscription
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class StartBikeSubscriptionService(
    private val repository: BikeSubscriptionRepository,
    private val processPort: BikeSubscriptionProcess
) : StartBikeSubscriptionUseCase {

    private val log = KotlinLogging.logger {}

    override fun start(command: StartBikeSubscriptionUseCase.Command): BikeSubscriptionId {
        val subscription = BikeSubscription(
            bikeId = command.bikeId,
            customerEmail = command.email,
            customerName = command.name
        )

        repository.save(subscription)
        log.info { "Bike subscription created: ${subscription.id}" }

        processPort.startSubscription(subscription.id, subscription.bikeId)
        log.info { "Bike subscription process started for: ${subscription.id}" }

        return subscription.id
    }
}
