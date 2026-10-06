package io.miragon.example.application.service.bike;

import io.miragon.example.application.port.inbound.bike.NotifyBikeCancelationUseCase;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifyBikeCancelationService implements NotifyBikeCancelationUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotifyBikeCancelationService.class);

    private final BikeSubscriptionRepository repository;

    public NotifyBikeCancelationService(BikeSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void notifyCancelation(BikeSubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);
        log.info("Notifying customer about cancelation: {}", subscription.customerEmail().value());
        repository.save(subscription.cancel());
    }

}
