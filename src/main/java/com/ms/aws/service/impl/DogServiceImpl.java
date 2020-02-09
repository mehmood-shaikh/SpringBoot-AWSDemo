package com.ms.aws.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.ms.aws.exception.IdNotFoundException;
import com.ms.aws.model.DogDynamoDB;
import com.ms.aws.model.RandomDogFromAPI;
import com.ms.aws.repository.DogRepository;
import com.ms.aws.service.DogService;
import com.ms.aws.service.PublicAPIService;

@Service
public class DogServiceImpl implements DogService {
	@Autowired
	AmazonS3 amazonS3Client;

	@Autowired
	AmazonDynamoDB amazonDynamoDBClient;

	@Autowired
	DogRepository dogRepository;
	
	@Autowired
	PublicAPIService publicAPIService;

	@Value("${app.s3.bucketName}")
	String bucketName;

	@Value("dog")
	String dynamoDbTableName;

	static Logger logger = LoggerFactory.getLogger(DogServiceImpl.class);

	@Override
	public DogDynamoDB create() {
		logger.debug("------- Inside POST method ---------");
		RandomDogFromAPI publicApiDogResponse = publicAPIService.getPublicAPIResponse();
		return uploadToAWS(publicApiDogResponse);
	}
	
	@Override
	public DogDynamoDB uploadToAWS(RandomDogFromAPI publicApiDogResponse) {
		DogDynamoDB dog = new DogDynamoDB();
		dog.setBreedName(publicApiDogResponse.getBreedName());
		dog.setS3Url(publicApiDogResponse.getMessage());
		dog.setUploadDate(new Date());
		dogRepository.save(dog);
		logger.debug("Dog saved to Dynamo DB: " + dog);
		dog.setS3Url(uploadFileToS3bucket(dog.getId(), publicApiDogResponse.getMessage(),
				publicApiDogResponse.getImageType()));
		logger.debug("Updated S3 URL in DynamoDB : " + dog);
		return dog;
	}

	private String uploadFileToS3bucket(String fileKey, String imageURL, String strImageType) {
		logger.debug("------- Uploading Image to S3 ---------");
		String retStr = null;
		try {
			InputStream inputStream = null;
			byte[] contents = null;

			inputStream = new URL(imageURL).openStream();
			contents = IOUtils.toByteArray(inputStream);

			InputStream stream = new ByteArrayInputStream(contents);

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(contents.length);
			meta.setContentType("image/" + strImageType);
			amazonS3Client.putObject(new PutObjectRequest(bucketName, fileKey, stream, meta)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			retStr = amazonS3Client.getUrl(bucketName, fileKey).toExternalForm();
			logger.debug("S3 URL of image: " + retStr);
			inputStream.close();
		} catch (IOException e) {
			logger.error("------- Error occured while uploading Image to S3 ---------");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return retStr;

	}

	@Override
	public boolean deleteDog(String id) throws IdNotFoundException {
		logger.debug("------- Inside DELETE method ---------");
		if (!dogRepository.existsById(id)) {
			logger.error("------- ID not found --------- : " + id);
			throw new IdNotFoundException(String.format("Dog ID : %s is not valid.", id));
		}
		try {
			dogRepository.deleteById(id);
			amazonS3Client.deleteObject(bucketName, id);
		} catch (Exception e) {
			logger.error("Error occured while deleting record.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Set<String> getDistinctBreedNames() {
		logger.debug("------- Inside GET breed names method ---------");
		Set<String> breedNameSet = new HashSet<>();
		for (DogDynamoDB dog : dogRepository.findAll()) {
			breedNameSet.add(dog.getBreedName());
		}
		return breedNameSet;
	}

}