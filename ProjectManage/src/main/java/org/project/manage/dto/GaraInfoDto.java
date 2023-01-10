package org.project.manage.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GaraInfoDto {
    
    private Long id;

    private String garaCode;
    
    private String garaName;

    private String phone;
    
    private String garaAddress;
    
    private boolean status;
    
    private Double latitude;
    
    private Double longitude;
    
    private String createdBy;
    
    private Date createdDate;
    
    private String docPath;

    private  Double discountRate;
}
