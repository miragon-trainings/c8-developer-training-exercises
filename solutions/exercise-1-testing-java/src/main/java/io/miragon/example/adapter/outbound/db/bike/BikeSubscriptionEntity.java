package io.miragon.example.adapter.outbound.db.bike;

import io.miragon.example.domain.bike.BikeSubscriptionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bike_subscriptions")
public class BikeSubscriptionEntity {

    @Id
    private UUID id;
    private UUID bikeId;
    private String customerEmail;
    private String customerName;
    @Enumerated(EnumType.STRING)
    private BikeSubscriptionStatus status;
    private LocalDateTime requestDate;
    private Boolean bikeAvailable;

    protected BikeSubscriptionEntity() {
    }

    public BikeSubscriptionEntity(
        UUID id,
        UUID bikeId,
        String customerEmail,
        String customerName,
        BikeSubscriptionStatus status,
        LocalDateTime requestDate,
        Boolean bikeAvailable
    ) {
        this.id = id;
        this.bikeId = bikeId;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.status = status;
        this.requestDate = requestDate;
        this.bikeAvailable = bikeAvailable;
    }

    public UUID getId() {
        return id;
    }

    public UUID getBikeId() {
        return bikeId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BikeSubscriptionStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public Boolean getBikeAvailable() {
        return bikeAvailable;
    }

}
