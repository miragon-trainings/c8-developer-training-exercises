package io.miragon.example.adapter.outbound.db.bike

import io.miragon.example.domain.bike.BikeSubscriptionStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "bike_subscriptions")
data class BikeSubscriptionEntity(
    @Id
    val id: UUID,
    val bikeId: UUID,
    val customerEmail: String,
    val customerName: String,
    @Enumerated(EnumType.STRING)
    val status: BikeSubscriptionStatus,
    val requestDate: LocalDateTime,
    val bikeAvailable: Boolean?
)
