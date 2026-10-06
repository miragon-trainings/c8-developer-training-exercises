package io.miragon.example.application.service;

import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionProcess;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.application.service.newsletter.ConfirmSubscriptionService;
import io.miragon.example.domain.NewsletterSubscription;
import io.miragon.example.domain.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.miragon.example.domain.TestObjectBuilder.testNewsletterSubscription;
import static org.mockito.Mockito.*;

class ConfirmSubscriptionServiceTest {

    private NewsletterSubscriptionRepository subscriptionRepository;
    private NewsletterSubscriptionProcess processPort;
    private ConfirmSubscriptionService underTest;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(NewsletterSubscriptionRepository.class);
        processPort = mock(NewsletterSubscriptionProcess.class);
        underTest = new ConfirmSubscriptionService(subscriptionRepository, processPort);
    }

    @Test
    void confirmSubscription() {
        // Given
        SubscriptionId subscriptionId = new SubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        NewsletterSubscription subscription = testNewsletterSubscription(subscriptionId);

        when(subscriptionRepository.find(subscriptionId)).thenReturn(subscription);

        // When
        underTest.confirm(subscriptionId);

        // Then
        verify(subscriptionRepository).find(subscriptionId);
        verify(subscriptionRepository).save(subscription);
        verify(processPort).confirmSubscription(subscriptionId);
        verifyNoMoreInteractions(subscriptionRepository, processPort);
    }
}
