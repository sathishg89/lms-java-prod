package com.lms.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lms.entity.User;

public class UserUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userEmail;
	private String password;
	private List<GrantedAuthority> authorites;

	public UserUserDetails(User lud) {
		userEmail = lud.getUserEmail();
		password = lud.getPassword();
		authorites = Arrays.stream(lud.getRole().split(",")).map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorites;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userEmail;
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

}
