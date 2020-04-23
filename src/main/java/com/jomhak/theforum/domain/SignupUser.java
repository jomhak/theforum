package com.jomhak.theforum.domain;

import javax.validation.constraints.NotBlank;

public class SignupUser {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String passwordCheck;
	
	@NotBlank
	private String email;
	
	public SignupUser() {
		
	}

	public SignupUser(@NotBlank String username, @NotBlank String password, @NotBlank String passwordCheck,
			@NotBlank String email) {
		super();
		this.username = username;
		this.password = password;
		this.passwordCheck = passwordCheck;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(@NotBlank String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(@NotBlank String password) {
		this.password = password;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public void setPasswordCheck(@NotBlank String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(@NotBlank String email) {
		this.email = email;
	}
	
	
	
	
}
