package org.project.manage.entities;

import org.hibernate.annotations.NaturalId;
import org.project.manage.security.ERole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users_customer")
@Setter
@Getter
public class UserCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuid;
	private String phonenumber;
	private String token;
    public UserCustomer(String cuid, String phonenumber, String token) {
    	this.cuid = cuid;
		this.phonenumber = phonenumber;
		this.token = token;
    }
}
