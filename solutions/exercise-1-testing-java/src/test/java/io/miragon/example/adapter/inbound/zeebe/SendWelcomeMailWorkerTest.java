package io.miragon.example.adapter.inbound.zeebe;

import io.miragon.example.adapter.inbound.zeebe.newsletter.SendWelcomeMailWorker;
import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Unit test for SendWelcomeMailWorker.
 * Tests that the worker correctly extracts variables and calls the use case.
 */
class SendWelcomeMailWorkerTest {
	
	private SendWelcomeMailUseCase useCase;
	private SendWelcomeMailWorker underTest;
	
	@BeforeEach
	void setUp() {
		useCase = mock(SendWelcomeMailUseCase.class);
		underTest = new SendWelcomeMailWorker(useCase);
	}
	
	@Test
	void shouldSendWelcomeMailWhenJobIsReceived() {
		// Given
		UUID subscriptionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		
		// When
		underTest.handle(subscriptionId.toString());
		
		// Then
		verify(useCase, times(1)).sendWelcomeMail(new SubscriptionId(subscriptionId));
		verifyNoMoreInteractions(useCase);
	}
}
