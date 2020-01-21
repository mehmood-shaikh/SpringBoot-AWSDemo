package com.ms.yabonza.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms.yabonza.exception.IdNotFoundException;
import com.ms.yabonza.model.DogDynamoDB;
import com.ms.yabonza.repository.DogRepository;
import com.ms.yabonza.service.DogService;

@RestController
public class DogController {

	@Autowired
	DogService dogService;

	@Autowired
	DogRepository dogRepository;

	@PostMapping("/dog")
	public DogDynamoDB create() {
		return dogService.create();
	}

	@GetMapping("/dog/{id}")
	public DogDynamoDB getDog(@PathVariable String id) {
		return dogRepository.findById(id)
				.orElseThrow(() -> new IdNotFoundException(String.format("Dog ID : %s is not valid.", id)));
	}

	@DeleteMapping("/dog/{id}")
	public boolean delete(@PathVariable String id) {
		return dogService.deleteDog(id);
	}

	@GetMapping("/dog")
	public List<DogDynamoDB> getDogsByBreedName(@RequestParam(name = "breedName") String breedName) {
		return dogRepository.findByBreedNameContaining(breedName);
	}

	@GetMapping("/breedNames")
	public Set<String> findDistinctBreedNames() {
		return dogService.getDistinctBreedNames();
	}

}
