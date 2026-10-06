package io.miragon.example.application.service.bike;

import io.miragon.example.application.port.inbound.bike.SendBikeWelcomeMailUseCase;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendBikeWelcomeMailService implements SendBikeWelcomeMailUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendBikeWelcomeMailService.class);

    private final BikeSubscriptionRepository repository;

    public SendBikeWelcomeMailService(BikeSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendWelcomeMail(BikeSubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        log.info("Sending welcome mail to new bike subscriber {}", subscription.customerEmail().value());
        repository.save(subscription.markActive());
    }

}
