package com.ms.aws.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;

@Configuration
public class SQSConfig {
	
	@Value("${cloud.aws.region.static}")
	private static String region;
	
	@Value("${app.sqs.QueueWithListenerName}")
	private static String queueWithListenerName;

    @Value("${app.sqs.QueueWithOutListenerName}")
    private static String queueWithOutListenerName;
    
    //private static Logger logger=LoggerFactory.getLogger(SQSConfig.class);
	
	public static AmazonSQS amazonSQS(AWSCredentialsProvider credentialsProvider ) {
        return AmazonSQSClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion(region)
            .build();
    }
	
	@Bean
    public static QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
	
	@Bean(name = "QueueWithListenerUrl")
    public  String createQueueWithListenerUrl(AmazonSQS amazonSQS, @Value("${app.sqs.QueueWithListenerName}")String queueWithListenerName) {
		CreateQueueResult sqsQueue = amazonSQS.createQueue(queueWithListenerName);
        return sqsQueue.getQueueUrl();
    }
	
	@Bean(name = "QueueWithOutListenerUrl")
    public String createQueueWithOutListenerUrl(AmazonSQS amazonSQS,@Value("${app.sqs.QueueWithOutListenerName}") String queueWithOutListenerName) {
		CreateQueueResult sqsQueue = amazonSQS.createQueue(queueWithOutListenerName);
        return sqsQueue.getQueueUrl();
    }

}
