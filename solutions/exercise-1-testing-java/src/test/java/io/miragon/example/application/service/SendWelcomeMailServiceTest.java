package io.miragon.example.application.service;

import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.application.service.newsletter.SendWelcomeMailService;
import io.miragon.example.domain.NewsletterSubscription;
import io.miragon.example.domain.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.miragon.example.domain.TestObjectBuilder.testNewsletterSubscription;
import static org.mockito.Mockito.*;

class SendWelcomeMailServiceTest {

    private NewsletterSubscriptionRepository subscriptionRepository;
    private SendWelcomeMailService underTest;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(NewsletterSubscriptionRepository.class);
        underTest = new SendWelcomeMailService(subscriptionRepository);
    }

    @Test
    void sendWelcomeMail() {
        // Given
        SubscriptionId subscriptionId = new SubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        NewsletterSubscription subscription = testNewsletterSubscription(subscriptionId);

        when(subscriptionRepository.find(subscriptionId)).thenReturn(subscription);

        // When
        underTest.sendWelcomeMail(subscriptionId);

        // Then
        verify(subscriptionRepository).find(subscriptionId);
        verifyNoMoreInteractions(subscriptionRepository);
    }
}
