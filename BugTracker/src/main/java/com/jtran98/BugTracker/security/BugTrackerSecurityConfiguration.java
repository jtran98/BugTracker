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
			.antMatchers("/index").permitAll()
			.antMatchers("/tickets/view-tickets/**").authenticated()
			.antMatchers("/tickets/take-ticket/**").hasAnyAuthority(AuthorityEnum.DEVELOPER.toString(), AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers("/tickets/drop-ticket/**").hasAnyAuthority(AuthorityEnum.DEVELOPER.toString(), AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers("/tickets/view-details/**").authenticated()
			.antMatchers("/tickets/make-comment").authenticated()
			.antMatchers("/tickets/modify-ticket/**").hasAnyAuthority(AuthorityEnum.DEVELOPER.toString(), AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.antMatchers("/tickets/delete-ticket/**").hasAnyAuthority(AuthorityEnum.PROJECTMANAGER.toString(), AuthorityEnum.ADMINISTRATOR.toString())
			.and()
			.formLogin()
			.loginPage("/login").permitAll()
			.failureUrl("/login-failed")
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index")
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
