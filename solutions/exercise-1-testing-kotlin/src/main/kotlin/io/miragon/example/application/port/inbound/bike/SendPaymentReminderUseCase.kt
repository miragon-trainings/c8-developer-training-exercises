package io.miragon.example.application.port.inbound.bike

import io.miragon.example.domain.bike.BikeSubscriptionId

interface SendPaymentReminderUseCase {
    fun sendPaymentReminder(subscriptionId: BikeSubscriptionId)
}
