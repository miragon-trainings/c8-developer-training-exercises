package io.miragon.example.domain.bike

import io.miragon.example.domain.Email
import io.miragon.example.domain.Name
import java.time.LocalDateTime

data class BikeSubscription(
    val id: BikeSubscriptionId = BikeSubscriptionId(java.util.UUID.randomUUID()),
    val bikeId: BikeId,
    val customerEmail: Email,
    val customerName: Name,
    val requestDate: LocalDateTime = LocalDateTime.now(),
    val status: BikeSubscriptionStatus = BikeSubscriptionStatus.PENDING,
    val bikeAvailable: Boolean? = null
) {
    fun markAvailability(available: Boolean) = copy(
        bikeAvailable = available,
        status = if (available) BikeSubscriptionStatus.AVAILABLE else BikeSubscriptionStatus.NOT_AVAILABLE
    )

    fun markPaymentReceived() = copy(status = BikeSubscriptionStatus.PAID)

    fun markShipped() = copy(status = BikeSubscriptionStatus.SHIPPED)

    fun markActive() = copy(status = BikeSubscriptionStatus.ACTIVE)

    fun cancel() = copy(status = BikeSubscriptionStatus.CANCELED)
}
