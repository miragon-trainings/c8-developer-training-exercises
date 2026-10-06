package io.miragon.example.application.service.newsletter

import io.miragon.example.application.port.inbound.newsletter.ConfirmSubscriptionUseCase
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionProcess
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository
import io.miragon.example.domain.SubscriptionId
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
@Transactional
class ConfirmSubscriptionService(
    private val repository: NewsletterSubscriptionRepository,
    private val processPort: NewsletterSubscriptionProcess
) : ConfirmSubscriptionUseCase {

    private val log = KotlinLogging.logger {}

    override fun confirm(subscriptionId: SubscriptionId) {
        val subscription = repository.find(subscriptionId)
        subscription.confirmRegistration()
        repository.save(subscription)
        processPort.confirmSubscription(subscription.id)
        log.info { "Confirmed subscription ${subscription.id}" }
    }
}
