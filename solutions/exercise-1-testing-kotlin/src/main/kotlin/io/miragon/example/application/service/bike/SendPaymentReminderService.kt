package io.miragon.example.application.service.bike

import io.miragon.example.application.port.inbound.bike.SendPaymentReminderUseCase
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SendPaymentReminderService(
    private val repository: BikeSubscriptionRepository
) : SendPaymentReminderUseCase {

    private val log = KotlinLogging.logger {}

    override fun sendPaymentReminder(subscriptionId: BikeSubscriptionId) {
        val subscription = repository.find(subscriptionId)
        log.info { "Sending payment reminder to ${subscription.customerEmail.value} for bike subscription ${subscription.id.value}" }
    }
}
