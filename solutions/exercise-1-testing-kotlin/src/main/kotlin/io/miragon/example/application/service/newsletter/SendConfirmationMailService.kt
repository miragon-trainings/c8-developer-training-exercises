package io.miragon.example.application.service.newsletter

import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository
import io.miragon.example.domain.SubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SendConfirmationMailService(
    private val repository: NewsletterSubscriptionRepository,
) : SendConfirmationMailUseCase {

    private val log = KotlinLogging.logger {}

    override fun sendConfirmationMail(subscriptionId: SubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Sending confirmation mail to ${subscription.email}" }
    }
}