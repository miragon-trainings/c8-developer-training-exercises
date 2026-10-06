package io.miragon.example.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestObjectBuilder {

    public static NewsletterSubscription testNewsletterSubscription() {
        return testNewsletterSubscription(
            new SubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
            new Name("John Doe"),
            new Email("john.doe@test.com"),
            new NewsletterId(UUID.fromString("f51d9793-1b24-45db-bd6f-dd4cb26795e6")),
            LocalDateTime.parse("2024-01-15T10:30:00"),
            SubscriptionStatus.PENDING
        );
    }

    public static NewsletterSubscription testNewsletterSubscription(SubscriptionId id) {
        return testNewsletterSubscription(
            id,
            new Name("John Doe"),
            new Email("john.doe@test.com"),
            new NewsletterId(UUID.fromString("f51d9793-1b24-45db-bd6f-dd4cb26795e6")),
            LocalDateTime.parse("2024-01-15T10:30:00"),
            SubscriptionStatus.PENDING
        );
    }

    public static NewsletterSubscription testNewsletterSubscription(SubscriptionStatus status) {
        return testNewsletterSubscription(
            new SubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
            new Name("John Doe"),
            new Email("john.doe@test.com"),
            new NewsletterId(UUID.fromString("f51d9793-1b24-45db-bd6f-dd4cb26795e6")),
            LocalDateTime.parse("2024-01-15T10:30:00"),
            status
        );
    }

    public static NewsletterSubscription testNewsletterSubscription(
        SubscriptionId id,
        Name name,
        Email email,
        NewsletterId newsletter,
        LocalDateTime registrationDate,
        SubscriptionStatus status
    ) {
        return new NewsletterSubscription(
            id,
            name,
            email,
            newsletter,
            registrationDate,
            status
        );
    }
}
