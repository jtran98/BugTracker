package com.jtran98.BugTracker.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jtran98.BugTracker.enums.AuthorityEnum;
import com.jtran98.BugTracker.model.User;

public class UserPrincipal implements UserDetails{
	
	private User user;
	
	public UserPrincipal(User user) {
		this.user = user;
	}
	public String getFirstName() {
		return user.getFirstName();
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> authorities = new ArrayList<>();
		GrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().toString());
		authorities.add(authority);
		return authorities;
	}
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}
	@Override
	public String getUsername() {
		return this.user.getUsername();
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
		return this.user.isActive();
	}

}
