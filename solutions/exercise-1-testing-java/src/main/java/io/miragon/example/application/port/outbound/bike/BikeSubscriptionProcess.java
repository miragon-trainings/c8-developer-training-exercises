package io.miragon.example.application.port.outbound.bike;

import io.miragon.example.domain.bike.BikeId;
import io.miragon.example.domain.bike.BikeSubscriptionId;

public interface BikeSubscriptionProcess {
    long startSubscription(BikeSubscriptionId id, BikeId bikeId);
    void sendPaymentReceived(BikeSubscriptionId id);
    void sendRequestCanceled(BikeSubscriptionId id);
    void sendBikeReceived(BikeSubscriptionId id);
}
