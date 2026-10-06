package io.miragon.example.adapter.outbound.db.newsletter;

import io.miragon.example.domain.SubscriptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "newsletter_subscription")
public class NewsletterSubscriptionEntity {

    @Id
    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionId;

    @Column(name = "subscriber_name", nullable = false)
    private String name;

    @Column(name = "subscriber_mail", nullable = false)
    private String email;

    @Column(name = "newsletter_id", nullable = false)
    private UUID newsletterId;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = false)
    private SubscriptionStatus status;

    protected NewsletterSubscriptionEntity() {
    }

    public NewsletterSubscriptionEntity(
        UUID subscriptionId,
        String name,
        String email,
        UUID newsletterId,
        LocalDateTime registrationDate,
        SubscriptionStatus status
    ) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.email = email;
        this.newsletterId = newsletterId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UUID getNewsletterId() {
        return newsletterId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

}
