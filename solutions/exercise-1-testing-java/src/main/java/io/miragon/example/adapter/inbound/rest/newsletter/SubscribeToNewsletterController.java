package io.miragon.example.adapter.inbound.rest.newsletter;

import io.miragon.example.application.port.inbound.newsletter.SubscribeToNewsletterUseCase;
import io.miragon.example.domain.Email;
import io.miragon.example.domain.Name;
import io.miragon.example.domain.NewsletterId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscribeToNewsletterController {

    private static final Logger log = LoggerFactory.getLogger(SubscribeToNewsletterController.class);

    private final SubscribeToNewsletterUseCase useCase;

    public SubscribeToNewsletterController(SubscribeToNewsletterUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Response> subscribeToNewsletter(@RequestBody SubscriptionForm input) {
        log.debug("Received REST-request to subscribe to newsletter: {}", input);
        var subscriptionId = useCase.subscribe(toCommand(input));
        return ResponseEntity.ok().body(new Response(subscriptionId.value().toString()));
    }

    public record SubscriptionForm(
        String email,
        String name,
        String newsletterId
    ) {
    }

    public record Response(String subscriptionId) {
    }

    private SubscribeToNewsletterUseCase.Command toCommand(SubscriptionForm form) {
        return new SubscribeToNewsletterUseCase.Command(
            new Email(form.email()),
            new Name(form.name()),
            new NewsletterId(UUID.fromString(form.newsletterId()))
        );
    }

}
