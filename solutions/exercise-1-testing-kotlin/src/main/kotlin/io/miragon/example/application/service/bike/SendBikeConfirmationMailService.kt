package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.SendBikeConfirmationMailUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SendBikeConfirmationMailService(
    private val repository: BikeSubscriptionRepository
) : SendBikeConfirmationMailUseCase {

    private val log = KotlinLogging.logger {}

    override fun sendConfirmationMail(subscriptionId: BikeSubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Sending bike confirmation mail to ${subscription.customerEmail.value} for bike ${subscription.bikeId.value}" }
    }
}
