package io.miragon.example.adapter.outbound.zeebe.newsletter;

import io.miragon.example.adapter.process.config.ProcessEngineApi;
import io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi;
import io.miragon.example.adapter.process.generated.Messages;
import io.miragon.example.domain.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NewsletterSubscriptionProcessAdapterTest {
	
	private ProcessEngineApi engineApi;
	private NewsletterSubscriptionProcessAdapter underTest;
	
	@BeforeEach
	void setUp() {
		engineApi = mock(ProcessEngineApi.class);
		underTest = new NewsletterSubscriptionProcessAdapter(engineApi);
	}
	
	@Test
	void startsNewsletterSubscriptionProcess() {
		// Given
		SubscriptionId subscriptionId = new SubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
		long expectedProcessInstanceKey = 42L;
		Map<String, Object> expectedVariables = Map.of("subscriptionId", subscriptionId.value().toString());
		when(engineApi.startProcess(any(), any())).thenReturn(expectedProcessInstanceKey);
		
		// When
		long result = underTest.submitForm(subscriptionId);
		
		// Then
		assertThat(result).isEqualTo(expectedProcessInstanceKey);
		verify(engineApi).startProcess(
				NewsletterSubscriptionProcessApi.PROCESS_ID,
				expectedVariables
		);
	}
	
	@Test
	void confirmSubscriptionSendsMessageWithCorrectParameters() {
		// Given
		SubscriptionId subscriptionId = new SubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
		
		// When
		underTest.confirmSubscription(subscriptionId);
		
		// Then
		verify(engineApi).sendMessage(
				Messages.SUBSCRIPTION_CONFIRMED,
				subscriptionId.value().toString()
		);
	}
}
