package com.ms.aws.model;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@DynamoDBTable(tableName = "dog")
@JsonIgnoreProperties({ "Id","UploadDate","S3Url"})
public class DogDynamoDB {

	private String id;
	private String breedName;
	private Date uploadDate;
	private String s3Url;
	
	@DynamoDBHashKey(attributeName = "ID")
    @DynamoDBAutoGeneratedKey
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@DynamoDBAttribute(attributeName = "BreedName")
	public String getBreedName() {
		return breedName;
	}
	public void setBreedName(String breedName) {
		this.breedName = breedName;
	}
	
	@DynamoDBAttribute(attributeName = "UploadDate")
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	@DynamoDBAttribute(attributeName = "S3Url")
	public String getS3Url() {
		return s3Url;
	}
	public void setS3Url(String s3Url) {
		this.s3Url = s3Url;
	}
	@Override
	public String toString() {
		return "DogDynamoDB [id=" + id + ", breedName=" + breedName + ", uploadDate=" + uploadDate + ", s3Url=" + s3Url + "]";
	}
	
	
}