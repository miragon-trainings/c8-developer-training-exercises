package io.miragon.example.application.service.newsletter;

import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.domain.SubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendConfirmationMailService implements SendConfirmationMailUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendConfirmationMailService.class);

    private final NewsletterSubscriptionRepository repository;

    public SendConfirmationMailService(NewsletterSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendConfirmationMail(SubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        log.info("Sending confirmation mail to {}", subscription.email());
    }

}
