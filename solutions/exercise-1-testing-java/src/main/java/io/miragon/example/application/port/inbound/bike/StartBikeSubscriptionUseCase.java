package io.miragon.example.application.port.inbound.bike;

import io.miragon.example.domain.Email;
import io.miragon.example.domain.Name;
import io.miragon.example.domain.bike.BikeId;
import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface StartBikeSubscriptionUseCase {
    record Command(
        BikeId bikeId,
        Email email,
        Name name
    ) {}

    BikeSubscriptionId start(Command command);
}
