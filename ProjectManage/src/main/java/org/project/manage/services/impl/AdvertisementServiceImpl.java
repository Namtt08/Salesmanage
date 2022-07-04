package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.project.manage.dto.DocMarketingDto;
import org.project.manage.entities.Document;
import org.project.manage.entities.MarketingImageEntity;
import org.project.manage.repository.DocumentRepository;
import org.project.manage.repository.MarketingImageRepository;
import org.project.manage.response.MakettingResponse;
import org.project.manage.services.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementServiceImpl implements AdvertisementService{
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private MarketingImageRepository marketingImageRepository;
	
	public static final String ADVERTISEMENT = "ADVERTISEMENT";

	@Override
	public MakettingResponse getAdvertisementList() {
		MakettingResponse response = new MakettingResponse();
		List <DocMarketingDto> listDocMarketing = new ArrayList<>();
		 List<Document> docInfo = documentRepository.findByDocType(ADVERTISEMENT);
		 for (Document document : docInfo) {
			 DocMarketingDto docMarketingDto = new DocMarketingDto();
			Optional<MarketingImageEntity> marketingImageEntity= marketingImageRepository.findByIdDocImage(document.getId());
			docMarketingDto.setDocPath(document.getDocPath());
			docMarketingDto.setUrl(marketingImageEntity.get().getUrl());
			docMarketingDto.setPriority(document.getPriority());
			docMarketingDto.setDocType(ADVERTISEMENT);
			docMarketingDto.setHeight(marketingImageEntity.get().getHeight());
			docMarketingDto.setWidth(marketingImageEntity.get().getWidth());
			listDocMarketing.add(docMarketingDto);
		}
		 response.setListDocMarketing(listDocMarketing);
		return response;
	}


}
