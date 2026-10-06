package io.miragon.example.adapter.out.db.message

import com.fasterxml.jackson.databind.ObjectMapper
import io.miragon.example.adapter.process.generated.Messages.FORM_SUBMITTED
import io.miragon.example.adapter.process.generated.Messages.SUBSCRIPTION_CONFIRMED
import io.miragon.example.application.port.out.NewsletterSubscriptionProcess
import io.miragon.example.domain.SubscriptionId
import org.springframework.stereotype.Component

/**
 * Outbox implementation of NewsletterSubscriptionProcess.
 * Instead of sending messages directly to Zeebe, this adapter saves them to the database.
 * A background scheduler (ProcessEngineOutboxScheduler) will send them asynchronously.
 */
@Component
class ProcessMessagePersistenceAdapter(
    private val repository: ProcessMessageJpaRepository,
    private val objectMapper: ObjectMapper
) : NewsletterSubscriptionProcess {

    override fun submitForm(id: SubscriptionId) {
        TODO("Implement outbox pattern: Create ProcessMessageEntity and save to repository")
        // HINT: messageName = FORM_SUBMITTED
        // HINT: correlationId = id.value.toString()
        // HINT: variables = serialize to JSON: mapOf("subscriptionId" to correlationId)
        // HINT: Use objectMapper.writeValueAsString() for JSON serialization
        // HINT: status = MessageStatus.PENDING (default)
        // HINT: Save the entity using repository.save()
        //
        // Why return nothing?
        // - Process instance key is not available yet (process hasn't started)
        // - The scheduler will send the message and start the process asynchronously
    }

    override fun confirmSubscription(id: SubscriptionId) {
        TODO("Implement outbox pattern: Create ProcessMessageEntity and save to repository")
        // HINT: messageName = SUBSCRIPTION_CONFIRMED
        // HINT: correlationId = id.value.toString()
        // HINT: variables = empty map (no variables needed for confirmation)
        // HINT: Serialize empty map to JSON string
        // HINT: Save the entity using repository.save()
    }
}
