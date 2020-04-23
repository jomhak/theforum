package com.jomhak.theforum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jomhak.theforum.control.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/images/background_tile.png").permitAll()
		
		.and()
		.authorizeRequests()
			.antMatchers("/",
						"/api/category/**",
						"/signup",
						"/category",
						"/category/{categoryName}",
						"/category/{categoryName}/{postId}",
						"/search",
						"/search/{searchedThing}").permitAll()
			.antMatchers("/profile",
						"/category/{categoryName}/new", 
						"/category/{categoryName}/{postId}/edit",
						"/category/{categoryName}/{postId}/comment",
						"/category/{categoryName}/{postId}/{commentId}/edit").authenticated()
			.antMatchers("/category/{categoryName}/{postId}/delete", 
						"/category/{categoryName}/{postId}/{commentId}/delete").authenticated()
			.antMatchers("/category/new",
						"/category/{categoryName}/edit",
						"/category/{categoryName}/delete").hasAuthority("admin")
			.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/")
			.permitAll()
		.and()
		.logout()
			.permitAll();
	}
	
	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
