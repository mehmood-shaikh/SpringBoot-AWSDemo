package com.ms.aws.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.ms.aws.model.DogDynamoDB;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.ms.aws.repository")
public class DynamoDBConfig {
	@Value("${cloud.dynamodb.endpoint}")
	private String dynamoDbEndpoint;

	@Value("${cloud.aws.accessKey}")
	private String awsAccessKey;

	@Value("${cloud.aws.secretKey}")
	private String awsSecretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	Logger logger = LoggerFactory.getLogger(DynamoDBConfig.class);

	@Bean
	public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProvider credentialsProvider) {
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider)
				.withEndpointConfiguration(new EndpointConfiguration(dynamoDbEndpoint, region)).build();
		DynamoDBMapper dynamoDBMapper;
		dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

		CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(DogDynamoDB.class);
		tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		logger.debug("------- Creating DynamoDb Table ---------");
		TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
		return amazonDynamoDB;
	}

}
