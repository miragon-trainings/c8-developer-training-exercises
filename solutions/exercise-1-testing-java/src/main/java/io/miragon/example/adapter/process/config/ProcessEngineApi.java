package io.miragon.example.adapter.process.config;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.search.response.ProcessInstance;
import io.miragon.bpmn.runtime.MessageName;
import io.miragon.bpmn.runtime.ProcessId;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class ProcessEngineApi {
	
	private final CamundaClient camundaClient;
	
	public ProcessEngineApi(CamundaClient camundaClient) {
		this.camundaClient = camundaClient;
	}
	
	/**
	 * Use this method to start a process instance via an undefined start event
	 *
	 * @param processId the id of the process to start
	 * @param variables the variables that should be passed to the process
	 * @return the key of the process instance
	 */
	public long startProcess(
			ProcessId processId,
			Map<String, Object> variables
	) {
		return camundaClient.newCreateInstanceCommand()
				.bpmnProcessId(processId.getValue())
				.latestVersion()
				.variables(variables)
				.send()
				.join()
				.getProcessInstanceKey();
	}
	
	/**
	 * Use this method to send a message to a running process instance.
	 *
	 * @param messageName   the id of the message that should be sent
	 * @param correlationId an id that is used to identify the process instance
	 * @param variables     the variables that should be passed to the process
	 */
	public void sendMessage(
			MessageName messageName,
			String correlationId,
			Map<String, Object> variables
	) {
		camundaClient.newPublishMessageCommand()
				.messageName(messageName.getValue())
				.correlationKey(correlationId)
				.variables(variables)
				.timeToLive(Duration.of(10, ChronoUnit.SECONDS))
				.send()
				.join();
	}
	
	public void sendMessage(MessageName messageName, String correlationId) {
		sendMessage(messageName, correlationId, Map.of());
	}
	
	/**
	 * Use this method to search for process instances.
	 * For usage in production I would recommend using filtering.
	 *
	 * @return a list of all process instances
	 */
	public List<ProcessInstance> searchForProcessInstances() {
		var instances = camundaClient.newProcessInstanceSearchRequest().send().join();
		return instances.items();
	}
	
}
