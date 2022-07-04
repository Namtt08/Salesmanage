package org.project.manage.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocMarketingDto {

	private String docPath;
	private String docType;
	private String url;
	private Long priority;
    private Long height;    
    private Long width;
    
	
}
