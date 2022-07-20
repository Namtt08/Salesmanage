package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "setting_marketing_info")
@Data
public class MarketingImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String url;
    
    private Long height;
    
    private Long width;
    
    private String note;
        
    private String docPath;
    
    private String docType;
    
    private String createdBy;
    
    private Date createdDate;
    
    private Date dateStart;
    
    private Date dateEnd;
    
    private boolean status;
    
    private Long userId;
    
    private String deleteBy;
    
    private Date deleteDate;
    
    private Long priority;
    

}
