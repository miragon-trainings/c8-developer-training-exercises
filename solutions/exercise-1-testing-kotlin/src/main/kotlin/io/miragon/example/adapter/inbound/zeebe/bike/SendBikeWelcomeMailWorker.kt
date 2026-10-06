package io.miragon.example.adapter.inbound.zeebe.bike

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.bike.SendBikeWelcomeMailUseCase
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SendBikeWelcomeMailWorker(
    private val useCase: SendBikeWelcomeMailUseCase
) {
    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.BIKE_SEND_WELCOME_MAIL)
    fun handle(@Variable subscriptionId: String) {
        log.info { "Sending welcome mail for subscription: $subscriptionId" }
        useCase.sendWelcomeMail(BikeSubscriptionId(UUID.fromString(subscriptionId)))
    }
}
