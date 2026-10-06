package io.miragon.example.domain;

import org.junit.jupiter.api.Test;

import static io.miragon.example.domain.TestObjectBuilder.testNewsletterSubscription;
import static org.assertj.core.api.Assertions.assertThat;

class NewsletterSubscriptionTest {

    @Test
    void confirmRegistrationChangesStatusToConfirmed() {
        NewsletterSubscription subscription = testNewsletterSubscription(SubscriptionStatus.PENDING);
        NewsletterSubscription confirmed = subscription.confirmRegistration();
        assertThat(confirmed.status()).isEqualTo(SubscriptionStatus.CONFIRMED);
        assertThat(confirmed.id()).isEqualTo(subscription.id());
    }

    @Test
    void abortRegistrationChangesStatusToAborted() {
        NewsletterSubscription subscription = testNewsletterSubscription(SubscriptionStatus.PENDING);
        NewsletterSubscription aborted = subscription.abortRegistration();
        assertThat(aborted.status()).isEqualTo(SubscriptionStatus.ABORTED);
        assertThat(aborted.id()).isEqualTo(subscription.id());
    }
}
