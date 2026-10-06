package io.miragon.example.application.port.inbound.newsletter;

import io.miragon.example.domain.Email;
import io.miragon.example.domain.Name;
import io.miragon.example.domain.NewsletterId;
import io.miragon.example.domain.SubscriptionId;

public interface SubscribeToNewsletterUseCase {

    record Command(
        Email email,
        Name name,
        NewsletterId newsletterId
    ) {}

    SubscriptionId subscribe(Command command);
}
