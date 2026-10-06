package io.miragon.example.application.service.bike;

import io.miragon.example.application.port.inbound.bike.StartBikeSubscriptionUseCase;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionProcess;
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscription;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StartBikeSubscriptionService implements StartBikeSubscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(StartBikeSubscriptionService.class);

    private final BikeSubscriptionRepository repository;
    private final BikeSubscriptionProcess processPort;

    public StartBikeSubscriptionService(
        BikeSubscriptionRepository repository,
        BikeSubscriptionProcess processPort
    ) {
        this.repository = repository;
        this.processPort = processPort;
    }

    @Override
    public BikeSubscriptionId start(Command command) {
        var subscription = new BikeSubscription(
            command.bikeId(),
            command.email(),
            command.name()
        );

        repository.save(subscription);
        log.info("Bike subscription created: {}", subscription.id());

        processPort.startSubscription(subscription.id(), subscription.bikeId());
        log.info("Bike subscription process started for: {}", subscription.id());

        return subscription.id();
    }

}
