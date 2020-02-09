package com.ms.aws.service;

import com.ms.aws.model.DogDynamoDB;
import com.ms.aws.model.RandomDogFromAPI;

public interface SQSService {
	void receiveMessage(RandomDogFromAPI message, String senderId);
	void sendToQueueWListener();
	void sendToQueueWOListener();
	DogDynamoDB getLatestfromQueue();
}
