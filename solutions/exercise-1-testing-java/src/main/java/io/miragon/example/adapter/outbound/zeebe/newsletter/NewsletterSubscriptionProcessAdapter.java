package io.miragon.example.adapter.outbound.zeebe.newsletter;

import io.miragon.example.adapter.process.config.ProcessEngineApi;
import io.miragon.example.adapter.process.generated.NewsletterSubscriptionProcessApi;
import io.miragon.example.adapter.process.generated.Messages;
import io.miragon.example.application.port.outbound.newsletter.NewsletterSubscriptionProcess;
import io.miragon.example.domain.SubscriptionId;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NewsletterSubscriptionProcessAdapter implements NewsletterSubscriptionProcess {

    private final ProcessEngineApi engineApi;

    public NewsletterSubscriptionProcessAdapter(ProcessEngineApi engineApi) {
        this.engineApi = engineApi;
    }

    @Override
    public long submitForm(SubscriptionId id) {
        Map<String, Object> variables = Map.of("subscriptionId", id.value().toString());
        return engineApi.startProcess(
            NewsletterSubscriptionProcessApi.PROCESS_ID,
            variables
        );
    }

    @Override
    public void confirmSubscription(SubscriptionId id) {
        engineApi.sendMessage(
            Messages.SUBSCRIPTION_CONFIRMED,
            id.value().toString()
        );
    }

}
