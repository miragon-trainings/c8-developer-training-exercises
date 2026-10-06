package io.miragon.example.application.service.bike;

import io.miragon.example.application.port.inbound.bike.SendPaymentReminderUseCase;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendPaymentReminderService implements SendPaymentReminderUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendPaymentReminderService.class);

    private final BikeSubscriptionRepository repository;

    public SendPaymentReminderService(BikeSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendPaymentReminder(BikeSubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        log.info("Sending payment reminder to {} for bike subscription {}",
            subscription.customerEmail().value(), subscription.id().value());
    }

}
