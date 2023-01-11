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
@Table(name = "user_introduced")
@Data
public class UserIntroducedEntity {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private Long userId;
	    
	    private Long userIntroducedId;

	    private String userType;
	    
	    private String createdBy;
	    
	    private Date createdDate;

		private Long rewardPoint;
    

}
