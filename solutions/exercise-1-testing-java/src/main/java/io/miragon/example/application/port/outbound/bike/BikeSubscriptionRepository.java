package io.miragon.example.application.port.outbound.bike;

import io.miragon.example.domain.bike.BikeSubscription;
import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface BikeSubscriptionRepository {
    BikeSubscription save(BikeSubscription subscription);
    BikeSubscription find(BikeSubscriptionId id);
    BikeSubscription search(BikeSubscriptionId id);
}
