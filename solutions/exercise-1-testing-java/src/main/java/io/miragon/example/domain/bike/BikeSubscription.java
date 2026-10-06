package io.miragon.example.domain.bike;

import io.miragon.example.domain.Email;
import io.miragon.example.domain.Name;

import java.time.LocalDateTime;
import java.util.UUID;

public record BikeSubscription(
        BikeSubscriptionId id,
        BikeId bikeId,
        Email customerEmail,
        Name customerName,
        LocalDateTime requestDate,
        BikeSubscriptionStatus status,
        Boolean bikeAvailable
) {
    public BikeSubscription(BikeId bikeId, Email customerEmail, Name customerName) {
        this(
                new BikeSubscriptionId(UUID.randomUUID()),
                bikeId,
                customerEmail,
                customerName,
                LocalDateTime.now(),
                BikeSubscriptionStatus.PENDING,
                null
        );
    }

    public BikeSubscription markAvailability(boolean available) {
        return new BikeSubscription(
                id,
                bikeId,
                customerEmail,
                customerName,
                requestDate,
                available ? BikeSubscriptionStatus.AVAILABLE : BikeSubscriptionStatus.NOT_AVAILABLE,
                available
        );
    }

    public BikeSubscription markPaymentReceived() {
        return new BikeSubscription(
                id,
                bikeId,
                customerEmail,
                customerName,
                requestDate,
                BikeSubscriptionStatus.PAID,
                bikeAvailable
        );
    }

    public BikeSubscription markShipped() {
        return new BikeSubscription(
                id,
                bikeId,
                customerEmail,
                customerName,
                requestDate,
                BikeSubscriptionStatus.SHIPPED,
                bikeAvailable
        );
    }

    public BikeSubscription markActive() {
        return new BikeSubscription(
                id,
                bikeId,
                customerEmail,
                customerName,
                requestDate,
                BikeSubscriptionStatus.ACTIVE,
                bikeAvailable
        );
    }

    public BikeSubscription cancel() {
        return new BikeSubscription(
                id,
                bikeId,
                customerEmail,
                customerName,
                requestDate,
                BikeSubscriptionStatus.CANCELED,
                bikeAvailable
        );
    }
}
