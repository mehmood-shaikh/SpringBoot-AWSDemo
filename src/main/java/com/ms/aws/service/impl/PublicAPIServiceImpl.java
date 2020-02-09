package com.ms.aws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ms.aws.model.RandomDogFromAPI;
import com.ms.aws.service.PublicAPIService;

@Service
public class PublicAPIServiceImpl implements PublicAPIService {

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${app.publicApi.url}")
	String publicAPI;
	
	static Logger logger = LoggerFactory.getLogger(PublicAPIServiceImpl.class);
	
	@Override
	public RandomDogFromAPI getPublicAPIResponse() {
		RandomDogFromAPI publicApiDogResponse = restTemplate.getForObject(publicAPI, RandomDogFromAPI.class);
		logger.debug("Public API Response : " + publicApiDogResponse);
		return publicApiDogResponse;
	}

}
