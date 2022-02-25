package org.project.manage.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.project.manage.entities.UserCustomer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String cuid;
	private String phoneNumber;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	public UserDetailsImpl(Long id, String cuid, String phoneNumber, String email,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.cuid = cuid;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.authorities = authorities;
	}
	public static UserDetailsImpl build(UserCustomer user) {
		return new UserDetailsImpl(
				user.getId(), 
				user.getCuid(), 
				user.getPhoneNumber(),
				user.getEmail(), 
				new ArrayList<GrantedAuthority>());
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public Long getId() {
		return id;
	}
	public String getCuid() {
		return cuid;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}
