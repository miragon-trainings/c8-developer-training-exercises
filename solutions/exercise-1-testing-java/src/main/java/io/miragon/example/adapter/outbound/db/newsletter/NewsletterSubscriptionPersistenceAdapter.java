package io.miragon.example.adapter.outbound.db.newsletter;

import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.domain.SubscriptionId;
import io.miragon.example.domain.NewsletterSubscription;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class NewsletterSubscriptionPersistenceAdapter implements NewsletterSubscriptionRepository {

    private final NewsletterSubscriptionJpaRepository repository;
    private final NewsletterSubscriptionEntityMapper mapper;

    public NewsletterSubscriptionPersistenceAdapter(
        NewsletterSubscriptionJpaRepository repository,
        NewsletterSubscriptionEntityMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NewsletterSubscription find(SubscriptionId subscriptionId) {
        var entity = repository.findBySubscriptionId(subscriptionId.value());
        if (entity == null) {
            throw new NoSuchElementException();
        }
        return mapper.toDomain(entity);
    }

    @Override
    public NewsletterSubscription search(SubscriptionId subscriptionId) {
        var entity = repository.findBySubscriptionId(subscriptionId.value());
        if (entity == null) {
            return null;
        }
        return mapper.toDomain(entity);
    }

    @Override
    public void save(NewsletterSubscription subscription) {
        var entity = mapper.toEntity(subscription);
        repository.save(entity);
    }

    @Override
    public void delete(SubscriptionId subscriptionId) {
        repository.deleteById(subscriptionId.value());
    }

}
