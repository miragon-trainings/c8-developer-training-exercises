package io.miragon.example.application.service.newsletter

import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository
import io.miragon.example.domain.SubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SendWelcomeMailService(
    private val repository: NewsletterSubscriptionRepository,
) : SendWelcomeMailUseCase {

    private val log = KotlinLogging.logger {}

    override fun sendWelcomeMail(subscriptionId: SubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Sending welcome mail to ${subscription.email}" }
    }
}