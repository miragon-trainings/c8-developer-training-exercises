package io.miragon.example.application.service.newsletter;

import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.domain.SubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendWelcomeMailService implements SendWelcomeMailUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendWelcomeMailService.class);

    private final NewsletterSubscriptionRepository repository;

    public SendWelcomeMailService(NewsletterSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendWelcomeMail(SubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        log.info("Sending welcome mail to {}", subscription.email());
    }

}
