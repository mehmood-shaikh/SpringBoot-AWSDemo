package com.ms.aws.repository;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.ms.aws.model.DogDynamoDB;

@EnableScan
public interface DogRepository extends CrudRepository<DogDynamoDB, String> {

	List<DogDynamoDB> findByBreedNameContaining(String breedName);

}
