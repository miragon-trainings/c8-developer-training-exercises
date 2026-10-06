package io.miragon.example.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewsletterSubscription(
        SubscriptionId id,
        Name name,
        Email email,
        NewsletterId newsletter,
        LocalDateTime registrationDate,
        SubscriptionStatus status
) {
    public NewsletterSubscription(Name name, Email email, NewsletterId newsletter) {
        this(
                new SubscriptionId(UUID.randomUUID()),
                name,
                email,
                newsletter,
                LocalDateTime.now(),
                SubscriptionStatus.PENDING
        );
    }

    public NewsletterSubscription confirmRegistration() {
        return new NewsletterSubscription(
                id,
                name,
                email,
                newsletter,
                registrationDate,
                SubscriptionStatus.CONFIRMED
        );
    }

    public NewsletterSubscription abortRegistration() {
        return new NewsletterSubscription(
                id,
                name,
                email,
                newsletter,
                registrationDate,
                SubscriptionStatus.ABORTED
        );
    }
}
