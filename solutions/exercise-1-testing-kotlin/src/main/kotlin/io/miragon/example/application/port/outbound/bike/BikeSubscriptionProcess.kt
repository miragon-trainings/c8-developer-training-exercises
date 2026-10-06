package io.miragon.example.application.port.outbound.bike

import io.miragon.example.domain.bike.BikeId
import io.miragon.example.domain.bike.BikeSubscriptionId

interface BikeSubscriptionProcess {
    fun startSubscription(id: BikeSubscriptionId, bikeId: BikeId): Long
    fun sendPaymentReceived(id: BikeSubscriptionId)
    fun sendRequestCanceled(id: BikeSubscriptionId)
    fun sendBikeReceived(id: BikeSubscriptionId)
}
