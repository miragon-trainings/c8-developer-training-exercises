package io.miragon.example.adapter.inbound.zeebe;

import io.miragon.example.adapter.inbound.zeebe.newsletter.SendConfirmationMailWorker;
import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Unit test for SendConfirmationMailWorker.
 * Tests that the worker correctly extracts variables and calls the use case.
 */
class SendConfirmationMailWorkerTest {
	
	private SendConfirmationMailUseCase useCase;
	private SendConfirmationMailWorker underTest;
	
	@BeforeEach
	void setUp() {
		useCase = mock(SendConfirmationMailUseCase.class);
		underTest = new SendConfirmationMailWorker(useCase);
	}
	
	@Test
	void shouldSendConfirmationMailWhenJobIsReceived() {
		// Given
		UUID subscriptionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		
		// When
		underTest.handle(subscriptionId.toString());
		
		// Then
		verify(useCase, times(1)).sendConfirmationMail(new SubscriptionId(subscriptionId));
		verifyNoMoreInteractions(useCase);
	}
}
