package io.miragon.example.adapter.inbound.zeebe.newsletter

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase
import io.miragon.example.domain.SubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*

@Component
class SendWelcomeMailWorker(
    private val useCase: SendWelcomeMailUseCase
) {

    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.NEWSLETTER_SEND_WELCOME_MAIL)
    fun handle(@Variable subscriptionId: String) {
        log.debug { "Received job to send welcome mail for subscriptionId: $subscriptionId" }
        useCase.sendWelcomeMail(SubscriptionId(UUID.fromString(subscriptionId)))
    }
}
