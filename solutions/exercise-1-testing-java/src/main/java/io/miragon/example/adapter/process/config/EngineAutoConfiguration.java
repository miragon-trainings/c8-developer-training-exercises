package io.miragon.example.adapter.process.config;

import io.camunda.client.CamundaClient;
import io.camunda.client.annotation.Deployment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Deployment(resources = {"classpath:bpmn/*.bpmn"})
@Import(ZeebeEnvironmentConfiguration.class)
public class EngineAutoConfiguration {

    @Bean
    public ProcessEngineApi processEngineApi(CamundaClient camundaClient) {
        return new ProcessEngineApi(camundaClient);
    }

}
