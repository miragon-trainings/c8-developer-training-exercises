package io.miragon.example.application.service.newsletter;

import io.miragon.example.application.port.inbound.newsletter.ConfirmSubscriptionUseCase;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionProcess;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.domain.SubscriptionId;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ConfirmSubscriptionService implements ConfirmSubscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConfirmSubscriptionService.class);

    private final NewsletterSubscriptionRepository repository;
    private final NewsletterSubscriptionProcess processPort;

    public ConfirmSubscriptionService(
        NewsletterSubscriptionRepository repository,
        NewsletterSubscriptionProcess processPort
    ) {
        this.repository = repository;
        this.processPort = processPort;
    }

    @Override
    public void confirm(SubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        subscription.confirmRegistration();
        repository.save(subscription);
        processPort.confirmSubscription(subscription.id());
        log.info("Confirmed subscription {}", subscription.id());
    }

}
