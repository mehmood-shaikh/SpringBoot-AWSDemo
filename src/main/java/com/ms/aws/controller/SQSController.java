package com.ms.aws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.aws.exception.EmptyQueueException;
import com.ms.aws.model.DogDynamoDB;
import com.ms.aws.service.SQSService;

@RestController
@RequestMapping("/sqs")
public class SQSController {

	@Autowired
	SQSService SQSService;
	
	//private static Logger logger=LoggerFactory.getLogger(SQSController.class);
	
	@PostMapping("/queueWithListener")
	public String sendToQueueWListener() {
		SQSService.sendToQueueWListener();
		return "Message Uploaded to Queue. It will be processed by SQS Listener";
	}
	
	@PostMapping("/queueWOListener")
	public String sendToQueueWOListener() {
		SQSService.sendToQueueWOListener();
		return "Message Uploaded to Queue.";
	}
	
	@GetMapping("/retrieve")
	public DogDynamoDB getLatestfromQueue() {
		DogDynamoDB dog=SQSService.getLatestfromQueue();
		if(dog == null) {
			throw new EmptyQueueException("SQS is empty. No record found to be retrieved");
		}
		return dog;
	}
	
	
}
