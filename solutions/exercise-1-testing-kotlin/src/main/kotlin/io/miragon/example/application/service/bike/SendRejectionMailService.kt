package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.SendRejectionMailUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SendRejectionMailService(
    private val repository: BikeSubscriptionRepository
) : SendRejectionMailUseCase {

    private val log = KotlinLogging.logger {}

    override fun sendRejectionMail(subscriptionId: BikeSubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Sending rejection mail to ${subscription.customerEmail.value} - bike ${subscription.bikeId.value} not available" }
    }
}
