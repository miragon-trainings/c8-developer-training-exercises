package io.miragon.example.adapter.outbound.db.newsletter

import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository
import io.miragon.example.domain.newsletter.NewsletterSubscription
import io.miragon.example.domain.SubscriptionId
import org.springframework.stereotype.Component

@Component
class NewsletterSubscriptionPersistenceAdapter(
    private val repository: NewsletterSubscriptionJpaRepository
) : NewsletterSubscriptionRepository {

    override fun find(subscriptionId: SubscriptionId): NewsletterSubscription {
        val entity = repository.findBySubscriptionId(subscriptionId.value) ?: throw NoSuchElementException()
        return NewsletterSubscriptionEntityMapper.toDomain(entity)
    }

    override fun search(subscriptionId: SubscriptionId): NewsletterSubscription? {
        val entity = repository.findBySubscriptionId(subscriptionId.value) ?: return null
        return NewsletterSubscriptionEntityMapper.toDomain(entity)
    }

    override fun save(subscription: NewsletterSubscription) {
        val entity = NewsletterSubscriptionEntityMapper.toEntity(subscription)
        repository.save(entity)
    }

    override fun delete(subscriptionId: SubscriptionId) {
        repository.deleteById(subscriptionId.value)
    }
}