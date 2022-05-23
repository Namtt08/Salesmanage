package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.project.manage.entities.Document;
import org.project.manage.repository.DocumentRepository;
import org.project.manage.services.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementServiceImpl implements AdvertisementService{
	
	@Autowired
	private DocumentRepository documentRepository;
	
	public static final String ADVERTISEMENT = "ADVERTISEMENT";

	@Override
	public List<Document> getAdvertisementList() {
		List<Document> response = new ArrayList<>();
		response = documentRepository.findByDocType(ADVERTISEMENT);
		return response;
	}


}
