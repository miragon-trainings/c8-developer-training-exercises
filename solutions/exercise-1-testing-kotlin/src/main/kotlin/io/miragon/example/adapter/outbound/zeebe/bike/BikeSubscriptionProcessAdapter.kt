package io.miragon.example.adapter.outbound.zeebe.bike

import io.miragon.example.adapter.process.generated.BikeSubscriptionSignupProcessApi
import io.miragon.example.adapter.process.generated.Messages
import io.miragon.example.application.port.outbound.bike.BikeSubscriptionProcess
import io.miragon.example.adapter.process.config.ProcessEngineApi
import io.miragon.example.domain.bike.BikeId
import io.miragon.example.domain.bike.BikeSubscriptionId
import org.springframework.stereotype.Component

@Component
class BikeSubscriptionProcessAdapter(
    private val engineApi: ProcessEngineApi
) : BikeSubscriptionProcess {

    override fun startSubscription(id: BikeSubscriptionId, bikeId: BikeId): Long {
        val variables = mapOf(
            "subscriptionId" to id.value.toString(),
            "bikeId" to bikeId.value.toString()
        )
        return engineApi.startProcess(
            processId = BikeSubscriptionSignupProcessApi.PROCESS_ID,
            variables = variables
        )
    }

    override fun sendPaymentReceived(id: BikeSubscriptionId) {
        engineApi.sendMessage(
            messageName = Messages.PAYMENT_RECEIVED,
            correlationId = id.value.toString()
        )
    }

    override fun sendRequestCanceled(id: BikeSubscriptionId) {
        engineApi.sendMessage(
            messageName = Messages.REQUEST_CANCELED,
            correlationId = id.value.toString()
        )
    }

    override fun sendBikeReceived(id: BikeSubscriptionId) {
        engineApi.sendMessage(
            messageName = Messages.BIKE_RECEIVED,
            correlationId = id.value.toString()
        )
    }
}
