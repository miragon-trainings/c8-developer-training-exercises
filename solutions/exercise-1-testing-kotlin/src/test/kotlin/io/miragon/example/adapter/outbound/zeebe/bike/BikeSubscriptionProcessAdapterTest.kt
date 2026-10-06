package io.miragon.example.adapter.outbound.zeebe.bike

import io.miragon.example.adapter.process.config.ProcessEngineApi
import io.miragon.example.adapter.process.generated.BikeSubscriptionSignupProcessApi
import io.miragon.example.adapter.process.generated.Messages
import io.miragon.example.domain.bike.BikeId
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class BikeSubscriptionProcessAdapterTest {

    private val engineApi = mockk<ProcessEngineApi>()
    private val underTest = BikeSubscriptionProcessAdapter(engineApi = engineApi)

    @Test
    fun `starts bike subscription process`() {

        // Given
        val subscriptionId = BikeSubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        val bikeId = BikeId(UUID.fromString("987e6543-e21b-34d5-b678-654321098000"))
        val expectedProcessInstanceKey = 42L
        val expectedVariables = mapOf(
            "subscriptionId" to subscriptionId.value.toString(),
            "bikeId" to bikeId.value.toString()
        )
        every { engineApi.startProcess(any(), any()) } returns expectedProcessInstanceKey

        // When
        val result = underTest.startSubscription(subscriptionId, bikeId)

        // Then
        assertThat(result).isEqualTo(expectedProcessInstanceKey)
        verify {
            engineApi.startProcess(
                processId = BikeSubscriptionSignupProcessApi.PROCESS_ID,
                variables = expectedVariables
            )
        }
    }

    @Test
    fun `sendPaymentReceived sends message with correct parameters`() {

        // Given
        val subscriptionId = BikeSubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        every { engineApi.sendMessage(any(), any(), any()) } just Runs

        // When
        underTest.sendPaymentReceived(subscriptionId)

        // Then
        verify {
            engineApi.sendMessage(
                messageName = Messages.PAYMENT_RECEIVED,
                correlationId = subscriptionId.value.toString(),
                variables = emptyMap()
            )
        }
    }

    @Test
    fun `sendRequestCanceled sends message with correct parameters`() {

        // Given
        val subscriptionId = BikeSubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        every { engineApi.sendMessage(any(), any(), any()) } just Runs

        // When
        underTest.sendRequestCanceled(subscriptionId)

        // Then
        verify {
            engineApi.sendMessage(
                messageName = Messages.REQUEST_CANCELED,
                correlationId = subscriptionId.value.toString(),
                variables = emptyMap()
            )
        }
    }

    @Test
    fun `sendBikeReceived sends message with correct parameters`() {

        // Given
        val subscriptionId = BikeSubscriptionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        every { engineApi.sendMessage(any(), any(), any()) } just Runs

        // When
        underTest.sendBikeReceived(subscriptionId)

        // Then
        verify {
            engineApi.sendMessage(
                messageName = Messages.BIKE_RECEIVED,
                correlationId = subscriptionId.value.toString(),
                variables = emptyMap()
            )
        }
    }
}
