package com.jtran98.BugTracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jtran98.BugTracker.enums.AuthorityEnum;
import com.jtran98.BugTracker.util.HomeEndpointConstants;
import com.jtran98.BugTracker.util.ProjectEndpointConstants;
import com.jtran98.BugTracker.util.TicketEndpointConstants;
import com.jtran98.BugTracker.util.UserEndpointConstants;

@Configuration
@EnableWebSecurity
public class BugTrackerSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserPrincipalDetailsService userPrincipalDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(getAuthenticationProvider());
	}
	
	/**
	 * Configurations for user security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(HomeEndpointConstants.INDEX).permitAll()
			//project endpoints
			.antMatchers(ProjectEndpointConstants.BASE).authenticated()
			.antMatchers(ProjectEndpointConstants.MANAGE_PROJECTS).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(ProjectEndpointConstants.UPDATE_PROJECT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(ProjectEndpointConstants.CREATE_NEW_PROJECT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(ProjectEndpointConstants.DELETE_PROJECT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(ProjectEndpointConstants.SAVE_PROJECT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			
			//user settings endpoints
			.antMatchers(UserEndpointConstants.BASE).authenticated()
			.antMatchers(UserEndpointConstants.SETTINGS).authenticated()
			.antMatchers(UserEndpointConstants.UPDATE_PASSWORD).authenticated()
			.antMatchers(UserEndpointConstants.UPDATE_USERNAME).authenticated()
			//user view endpoints
			.antMatchers(UserEndpointConstants.USER_DETAILS).hasAnyAuthority(AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.REDIRECT_VIEW_USER).hasAnyAuthority(AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.PROJECT_MEMBERS).hasAnyAuthority(AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.REDIRECT_VIEW_USERS).hasAnyAuthority(AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			//usermanagement endpoints
			.antMatchers(UserEndpointConstants.DISABLE_ACCOUNT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.ENABLE_ACCOUNT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.ASSIGN_USER_TO_PROJECT).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.ASSIGN_USER_TO_ROLE).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.CREATE_NEW_USER).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.SAVE_USER).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(UserEndpointConstants.MANAGE_USERS).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			
			//ticket endpoints for basic CRU functions
			.antMatchers(TicketEndpointConstants.BASE).authenticated()
			.antMatchers(TicketEndpointConstants.VIEW_DETAILS).authenticated()
			.antMatchers(TicketEndpointConstants.TICKET_DETAILS).authenticated()
			.antMatchers(TicketEndpointConstants.SUBMITTED_TICKETS).authenticated()
			.antMatchers(TicketEndpointConstants.PROJECT_TICKETS).authenticated()
			.antMatchers(TicketEndpointConstants.SAVE_TICKET).authenticated()
			.antMatchers(TicketEndpointConstants.SUBMIT_NEW_TICKET).authenticated()
			.antMatchers(TicketEndpointConstants.UPDATE_TICKET).authenticated()
			.antMatchers(TicketEndpointConstants.VIEW_DETAILS).authenticated()
			.antMatchers(TicketEndpointConstants.MAKE_COMMENT).authenticated()
			.antMatchers(TicketEndpointConstants.UPLOAD_FILE).authenticated()
			.antMatchers(TicketEndpointConstants.DOWNLOAD_FILE).authenticated()
			//ticket endpoints for assigning
			.antMatchers(TicketEndpointConstants.ASSIGNED_TICKETS).hasAnyAuthority(AuthorityEnum.DEVELOPER.toString(), AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(TicketEndpointConstants.DROP_TICKET).hasAnyAuthority(AuthorityEnum.DEVELOPER.toString(), AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers(TicketEndpointConstants.TAKE_TICKET).hasAnyAuthority(AuthorityEnum.DEVELOPER.toString(), AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			//ticket endpoint for deleting
			.antMatchers(TicketEndpointConstants.DELETE_TICKET).hasAnyAuthority(AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			//admin endpoint for viewing all tickets
			.antMatchers(TicketEndpointConstants.MANAGE_TICKETS).hasAuthority(AuthorityEnum.ADMINISTRATOR.toString())
			
			.and()
			.formLogin()
			.loginPage("/"+HomeEndpointConstants.LOGIN).permitAll()
			.failureUrl("/"+HomeEndpointConstants.LOGIN_FAILED)
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/"+HomeEndpointConstants.LOGOUT)).logoutSuccessUrl("/"+HomeEndpointConstants.INDEX)
			.and()
			.rememberMe().tokenValiditySeconds(2592000).rememberMeParameter("rememberMe").userDetailsService(userPrincipalDetailsService);
	}
	
	@Bean
	DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
		return daoAuthenticationProvider;
	}
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
