package com.ms.aws.service;

import java.util.Set;

import com.ms.aws.exception.IdNotFoundException;
import com.ms.aws.model.DogDynamoDB;

public interface DogService {
	
	DogDynamoDB create();

	boolean deleteDog(String id) throws IdNotFoundException;

	Set<String> getDistinctBreedNames();
	

}
