package io.miragon.example.application.service;

import io.miragon.example.application.port.inbound.newsletter.SubscribeToNewsletterUseCase;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionProcess;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionRepository;
import io.miragon.example.application.service.newsletter.SubscribeToNewsletterService;
import io.miragon.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SubscribeToNewsletterServiceTest {

    private NewsletterSubscriptionProcess processPort;
    private NewsletterSubscriptionRepository subscriptionRepository;
    private SubscribeToNewsletterService underTest;

    @BeforeEach
    void setUp() {
        processPort = mock(NewsletterSubscriptionProcess.class);
        subscriptionRepository = mock(NewsletterSubscriptionRepository.class);
        underTest = new SubscribeToNewsletterService(subscriptionRepository, processPort);
    }

    @Test
    void createSubscription() {
        // Given
        ArgumentCaptor<NewsletterSubscription> captor = ArgumentCaptor.forClass(NewsletterSubscription.class);
        UUID newsletterId = UUID.fromString("f51d9793-1b24-45db-bd6f-dd4cb26795e6");
        when(processPort.submitForm(any(SubscriptionId.class))).thenReturn(1L);

        SubscribeToNewsletterUseCase.Command command = new SubscribeToNewsletterUseCase.Command(
            new Email("john.doe@test.com"),
            new Name("John Doe"),
            new NewsletterId(newsletterId)
        );

        // When
        SubscriptionId subscription = underTest.subscribe(command);

        // Then
        assertThat(subscription).isNotNull();
        verify(processPort).submitForm(subscription);
        verify(subscriptionRepository).save(captor.capture());
        verifyNoMoreInteractions(processPort, subscriptionRepository);
    }
}
