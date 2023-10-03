package com.prodapt.learningspring.business;

import lombok.Data;

@Data
public class LoginBody {
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setName(String name) {
		this.username = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}