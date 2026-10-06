package io.miragon.example.adapter.inbound.zeebe.newsletter

import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import io.camunda.client.api.response.ActivatedJob
import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase
import io.miragon.example.domain.SubscriptionId
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*

@Component
class SendConfirmationMailWorker(
    private val useCase: SendConfirmationMailUseCase
) {

    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.NEWSLETTER_SEND_CONFIRMATION_MAIL)
    fun handle(job: ActivatedJob, @Variable subscriptionId: String) {
        log.debug { "Received job to send confirmation mail for subscriptionId: $subscriptionId" }
        useCase.sendConfirmationMail(SubscriptionId(UUID.fromString(subscriptionId)))
    }
}
