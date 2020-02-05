package com.ms.aws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomDogFromAPI {

	private String message;
	private String status;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "RandomDogFromAPI [message=" + message + ", status=" + status + "]";
	}
	
	public String getBreedName() {
		String[] splitArr=this.message.split("/");
		return splitArr[splitArr.length-2];
	}
	
	public String getImageType() {
		String[] splitArr=this.message.split("\\.");
		return splitArr[splitArr.length-1];
	}
	
}
