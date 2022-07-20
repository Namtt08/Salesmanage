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
		List<MarketingImageEntity> listMarketingImage = marketingImageRepository.findAll();
		for (MarketingImageEntity marketingImageDto : listMarketingImage) {
			 DocMarketingDto docMarketingDto = new DocMarketingDto();
			docMarketingDto.setDocPath(marketingImageDto.getDocPath());
			docMarketingDto.setUrl(marketingImageDto.getUrl());
			docMarketingDto.setPriority(marketingImageDto.getPriority());
			docMarketingDto.setDocType(ADVERTISEMENT);
			docMarketingDto.setHeight(marketingImageDto.getHeight());
			docMarketingDto.setWidth(marketingImageDto.getWidth());
			listDocMarketing.add(docMarketingDto);
			
		}
		 response.setListDocMarketing(listDocMarketing);
		return response;
	}


}
