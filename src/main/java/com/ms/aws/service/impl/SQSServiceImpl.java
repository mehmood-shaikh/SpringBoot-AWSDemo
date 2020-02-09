package com.ms.aws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.aws.model.DogDynamoDB;
import com.ms.aws.model.RandomDogFromAPI;
import com.ms.aws.service.DogService;
import com.ms.aws.service.PublicAPIService;
import com.ms.aws.service.SQSService;

@Service
public class SQSServiceImpl implements SQSService {

	@Value("${app.sqs.QueueWithListenerName}")
	String queueWithListenerName;
	
	@Value("${app.sqs.QueueWithOutListenerName}")
	String queueWithOutListenerName;

	@Autowired
	@Qualifier("QueueWithOutListenerUrl")
	String queueWithOutListenerURL;

	@Autowired
	DogService dogService;

	@Autowired
	PublicAPIService publicAPIService;

	@Autowired
	AmazonSQS amazonSQS;

	Logger logger = LoggerFactory.getLogger(SQSServiceImpl.class);

	@Autowired
	QueueMessagingTemplate queueMessagingTemplate;

	@Override
	@SqsListener("${app.sqs.QueueWithListenerName}")
	public void receiveMessage(RandomDogFromAPI message, @Header("SenderId") String senderId) {
		logger.info("Received message: {}, having SenderId: {}", message, senderId);
		dogService.uploadToAWS(message);

	}

	@Override
	public void sendToQueueWListener() {
		logger.debug("------- Inside POST to Queue with Listener method ---------");
		RandomDogFromAPI publicApiDogResponse = publicAPIService.getPublicAPIResponse();

		try {
			Thread.sleep((long) (Math.random() * 10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("------- Thread sleeping ---------");
		queueMessagingTemplate.convertAndSend(queueWithListenerName, publicApiDogResponse);
		logger.debug("Message uploaded to SQS.");
	}

	@Override
	public void sendToQueueWOListener() {
		logger.debug("------- Inside POST to Queue without Listener method ---------");
		RandomDogFromAPI publicApiDogResponse = publicAPIService.getPublicAPIResponse();
		queueMessagingTemplate.convertAndSend(queueWithOutListenerName, publicApiDogResponse);
		logger.debug("Message uploaded to SQS.");
	}

	@Override
	public DogDynamoDB getLatestfromQueue() {
		logger.debug("------- Inside GET from Queue without Listener method ---------");
		ReceiveMessageRequest request = new ReceiveMessageRequest(queueWithOutListenerName).withVisibilityTimeout(10)
			      .withWaitTimeSeconds(5);
        request.setMaxNumberOfMessages(1);

        ReceiveMessageResult result = null;
        result = amazonSQS.receiveMessage(request);
        DogDynamoDB dog=null;
        if(result.getMessages().size() > 0) {
        	try {
        		RandomDogFromAPI randomDog= new ObjectMapper().readValue(result.getMessages().get(0).getBody(), RandomDogFromAPI.class);
				logger.info("Received message: {}, having RequestId: {}", randomDog, result.getSdkResponseMetadata().getRequestId());
				dog=dogService.uploadToAWS(randomDog);
				
				amazonSQS.deleteMessage(queueWithOutListenerURL, result.getMessages().get(0).getReceiptHandle());
				logger.debug("Message deleted from SQS.");
			} catch (Exception e) {
				e.printStackTrace();
			} 	
        }
		return dog;
	}

}
