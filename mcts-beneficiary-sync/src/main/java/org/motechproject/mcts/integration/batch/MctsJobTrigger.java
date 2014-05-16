package org.motechproject.mcts.integration.batch;

import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MctsJobTrigger {
	
	 private final static Logger LOGGER = LoggerFactory.getLogger(MctsJobTrigger.class);
	 private RestTemplate restTemplate;
	 private BatchServiceUrlGenerator batchServiceUrlGenerator;
	 @Autowired
	 public MctsJobTrigger(@Qualifier("mctsRestTemplate") RestTemplate restTemplate, BatchServiceUrlGenerator batchServiceUrlGenerator) {
	        this.restTemplate = restTemplate;
	        this.batchServiceUrlGenerator = batchServiceUrlGenerator;
	    }
	   
		public void triggerJob() {
	        LOGGER.info("Started service to trigger mcts job with batch");
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            try {
           	    System.out.println("path"+batchServiceUrlGenerator.getScheduleBatchUrl());
           	    restTemplate.getForObject("http://localhost:8080/motech-platform-batch/batch/trigger/mcts-job", String.class);
            }
            catch(Exception e) {
           	    LOGGER.info(e.getMessage());
           }
	       

	    }
	    
	    
	  
}