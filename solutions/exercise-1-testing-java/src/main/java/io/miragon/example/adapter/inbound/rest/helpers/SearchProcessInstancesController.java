package io.miragon.example.adapter.inbound.rest.helpers;

import io.miragon.example.adapter.process.config.ProcessEngineApi;
import io.camunda.client.api.search.response.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Not part of the use-case.
 * Exists to demonstrate process-engine query implementation and test responses.
 */
@RestController
@RequestMapping("/api/process-instances")
public class SearchProcessInstancesController {

    private static final Logger log = LoggerFactory.getLogger(SearchProcessInstancesController.class);

    private final ProcessEngineApi processEngineApi;

    public SearchProcessInstancesController(ProcessEngineApi processEngineApi) {
        this.processEngineApi = processEngineApi;
    }

    @GetMapping
    public ResponseEntity<List<ProcessInstance>> getAllProcessInstances() {
        log.debug("Received REST-request to query process instances");
        var instances = processEngineApi.searchForProcessInstances();
        return ResponseEntity.ok().body(instances);
    }

}
