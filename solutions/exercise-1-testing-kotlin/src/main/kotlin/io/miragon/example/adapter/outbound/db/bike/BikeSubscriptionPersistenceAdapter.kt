package io.miragon.example.adapter.outbound.db.bike

import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository
import io.miragon.example.domain.bike.BikeSubscription
import io.miragon.example.domain.bike.BikeSubscriptionId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BikeSubscriptionPersistenceAdapter(
    private val jpaRepository: BikeSubscriptionJpaRepository,
    private val mapper: BikeSubscriptionEntityMapper
) : BikeSubscriptionRepository {

    override fun save(subscription: BikeSubscription): BikeSubscription {
        val entity = mapper.toEntity(subscription)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun find(id: BikeSubscriptionId): BikeSubscription {
        val entity = jpaRepository.findByIdOrNull(id.value)
            ?: throw NoSuchElementException("BikeSubscription with id ${id.value} not found")
        return mapper.toDomain(entity)
    }

    override fun search(id: BikeSubscriptionId): BikeSubscription? {
        return jpaRepository.findByIdOrNull(id.value)?.let { mapper.toDomain(it) }
    }
}
