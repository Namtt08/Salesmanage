package org.project.manage.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "document_contract")

@Data
public class DocumentContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userLeasedId;
    
    private String contractCode;

    private String docPath;
    
    private Date deletedDate;
    
	private Date createdDate;
	
	private Date modifiedDate;
	
	private String createdBy;
	
	private String modifiedBy;
	
    private String docType;
	




}
