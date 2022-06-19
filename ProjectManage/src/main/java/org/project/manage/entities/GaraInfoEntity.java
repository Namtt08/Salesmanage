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
@Table(name = "gara_info")
@Data
public class GaraInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    

}
