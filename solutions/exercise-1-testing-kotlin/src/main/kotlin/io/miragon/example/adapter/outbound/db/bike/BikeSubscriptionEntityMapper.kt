package io.miragon.example.adapter.outbound.db.bike

import io.miragon.example.domain.Email
import io.miragon.example.domain.Name
import io.miragon.example.domain.bike.BikeId
import io.miragon.example.domain.bike.BikeSubscription
import io.miragon.example.domain.bike.BikeSubscriptionId
import org.springframework.stereotype.Component

@Component
class BikeSubscriptionEntityMapper {

    fun toDomain(entity: BikeSubscriptionEntity): BikeSubscription {
        return BikeSubscription(
            id = BikeSubscriptionId(entity.id),
            bikeId = BikeId(entity.bikeId),
            customerEmail = Email(entity.customerEmail),
            customerName = Name(entity.customerName),
            requestDate = entity.requestDate,
            status = entity.status,
            bikeAvailable = entity.bikeAvailable
        )
    }

    fun toEntity(domain: BikeSubscription): BikeSubscriptionEntity {
        return BikeSubscriptionEntity(
            id = domain.id.value,
            bikeId = domain.bikeId.value,
            customerEmail = domain.customerEmail.value,
            customerName = domain.customerName.value,
            requestDate = domain.requestDate,
            status = domain.status,
            bikeAvailable = domain.bikeAvailable
        )
    }
}
