package io.miragon.example.adapter.out.db.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.miragon.example.adapter.process.generated.Messages;
import io.miragon.example.application.port.out.NewsletterSubscriptionProcess;
import io.miragon.example.domain.SubscriptionId;
import org.springframework.stereotype.Component;

/**
 * Outbox implementation of NewsletterSubscriptionProcess.
 * Instead of sending messages directly to Zeebe, this adapter saves them to the database.
 * A background scheduler (ProcessEngineOutboxScheduler) will send them asynchronously.
 */
@Component
public class ProcessMessagePersistenceAdapter implements NewsletterSubscriptionProcess {

    private final ProcessMessageJpaRepository repository;
    private final ObjectMapper objectMapper;

    public ProcessMessagePersistenceAdapter(
            ProcessMessageJpaRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void submitForm(SubscriptionId id) {
        throw new UnsupportedOperationException("TODO: Implement outbox pattern: Create ProcessMessageEntity and save to repository");
        // HINT: messageName = Messages.FORM_SUBMITTED
        // HINT: correlationId = id.value().toString()
        // HINT: variables = serialize to JSON: Map.of("subscriptionId", correlationId)
        // HINT: Use objectMapper.writeValueAsString() for JSON serialization
        // HINT: status = MessageStatus.PENDING (default)
        // HINT: Save the entity using repository.save()
        //
        // Why return nothing?
        // - Process instance key is not available yet (process hasn't started)
        // - The scheduler will send the message and start the process asynchronously
    }

    @Override
    public void confirmSubscription(SubscriptionId id) {
        throw new UnsupportedOperationException("TODO: Implement outbox pattern: Create ProcessMessageEntity and save to repository");
        // HINT: messageName = Messages.SUBSCRIPTION_CONFIRMED
        // HINT: correlationId = id.value().toString()
        // HINT: variables = empty map (no variables needed for confirmation)
        // HINT: Serialize empty map to JSON string
        // HINT: Save the entity using repository.save()
    }
}
