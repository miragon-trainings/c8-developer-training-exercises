package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.NotifyBikeCancelationUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class NotifyBikeCancelationService(
    private val repository: BikeSubscriptionRepository
) : NotifyBikeCancelationUseCase {

    private val log = KotlinLogging.logger {}

    override fun notifyCancelation(subscriptionId: BikeSubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Notifying customer about cancelation: ${subscription.customerEmail.value}" }
        repository.save(subscription.cancel())
    }
}
