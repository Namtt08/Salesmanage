package org.project.manage.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "cuid"),
		@UniqueConstraint(columnNames = "phoneNumber"), @UniqueConstraint(columnNames = "username") })
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String cuid;
	private String username;
	private String password;
	private String phoneNumber;
	private String phoneNumber2;
	private String email;
	private boolean isBlockUser = false;
	private String nationalId;
	private String fullName;
	private String gender;
	private String avatar;
	private Date createdDate;
	private Date modifiedDate;
	private String userType;
	private Date dob;
	private Long point_lv;
	private Long point;
	private String tokenFirebase ;
    private Date deleteDate;
    private String deleteBy;
	

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	public User() {
	}
}
