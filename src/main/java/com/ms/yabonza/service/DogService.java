package com.ms.yabonza.service;

import java.util.Set;

import com.ms.yabonza.exception.IdNotFoundException;
import com.ms.yabonza.model.DogDynamoDB;

public interface DogService {
	
	DogDynamoDB create();

	boolean deleteDog(String id) throws IdNotFoundException;

	Set<String> getDistinctBreedNames();
	

}
