package io.miragon.example.adapter.outbound.db.bike;

import io.miragon.example.domain.Email;
import io.miragon.example.domain.Name;
import io.miragon.example.domain.bike.BikeId;
import io.miragon.example.domain.bike.BikeSubscription;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.springframework.stereotype.Component;

@Component
public class BikeSubscriptionEntityMapper {

    public BikeSubscription toDomain(BikeSubscriptionEntity entity) {
        return new BikeSubscription(
            new BikeSubscriptionId(entity.getId()),
            new BikeId(entity.getBikeId()),
            new Email(entity.getCustomerEmail()),
            new Name(entity.getCustomerName()),
            entity.getRequestDate(),
            entity.getStatus(),
            entity.getBikeAvailable()
        );
    }

    public BikeSubscriptionEntity toEntity(BikeSubscription domain) {
        return new BikeSubscriptionEntity(
            domain.id().value(),
            domain.bikeId().value(),
            domain.customerEmail().value(),
            domain.customerName().value(),
            domain.status(),
            domain.requestDate(),
            domain.bikeAvailable()
        );
    }

}
