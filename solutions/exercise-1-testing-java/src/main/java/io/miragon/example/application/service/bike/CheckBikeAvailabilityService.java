package io.miragon.example.application.service.bike;

import io.miragon.example.application.port.inbound.bike.CheckBikeAvailabilityUseCase;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CheckBikeAvailabilityService implements CheckBikeAvailabilityUseCase {

    private static final Logger log = LoggerFactory.getLogger(CheckBikeAvailabilityService.class);

    private final BikeSubscriptionRepository repository;
    private final Random random = new Random();

    public CheckBikeAvailabilityService(BikeSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkAvailability(BikeSubscriptionId subscriptionId) {
        var subscription = repository.find(subscriptionId);

        // Mock availability check: 80% chance of being available
        boolean available = random.nextDouble() < 0.8;

        log.info("Bike {} availability check: {}", subscription.bikeId().value(), available);

        repository.save(subscription.markAvailability(available));

        return available;
    }

}
