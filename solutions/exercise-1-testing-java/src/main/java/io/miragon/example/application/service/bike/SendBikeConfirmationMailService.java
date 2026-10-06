package io.miragon.example.application.service.bike;

import io.miragon.example.application.port.inbound.bike.SendBikeConfirmationMailUseCase;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendBikeConfirmationMailService implements SendBikeConfirmationMailUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendBikeConfirmationMailService.class);

    private final BikeSubscriptionRepository repository;

    public SendBikeConfirmationMailService(BikeSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendConfirmationMail(BikeSubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        log.info("Sending bike confirmation mail to {} for bike {}",
            subscription.customerEmail().value(), subscription.bikeId().value());
    }

}
