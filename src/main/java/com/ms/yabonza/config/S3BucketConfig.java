package com.ms.yabonza.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3BucketConfig {

	@Value("${cloud.dynamodb.endpoint}")
	private String dynamoDbEndpoint;

	@Value("${cloud.aws.accessKey}")
	private String awsAccessKey;

	@Value("${cloud.aws.secretKey}")
	private String awsSecretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${app.s3.bucketName}")
	String bucketName;

	Logger logger = LoggerFactory.getLogger(S3BucketConfig.class);

	@Bean
	public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider) {
		AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider)
				.withRegion(region).build();
		if (!amazonS3Client.doesBucketExistV2(bucketName)) {
			logger.debug("------- Creating new bucket : " + bucketName + " ---------");
			amazonS3Client.createBucket(bucketName);
		}
		return amazonS3Client;
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
	}

	@Bean
	public AWSCredentialsProvider amazonAWSCredentialsProvider(AWSCredentials amazonAWSCredentials) {
		return new AWSStaticCredentialsProvider(amazonAWSCredentials);

	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
