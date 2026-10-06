package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.SendBikeWelcomeMailUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SendBikeWelcomeMailService(
    private val repository: BikeSubscriptionRepository
) : SendBikeWelcomeMailUseCase {

    private val log = KotlinLogging.logger {}

    override fun sendWelcomeMail(subscriptionId: BikeSubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Sending welcome mail to new bike subscriber ${subscription.customerEmail.value}" }
        repository.save(subscription.markActive())
    }
}
