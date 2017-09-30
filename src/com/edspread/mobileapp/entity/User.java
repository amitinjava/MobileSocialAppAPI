package com.edspread.mobileapp.entity;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3663196118865841394L;
	public Integer id;
	public String email;
	public String password;
	public String registrationCode;
	public Boolean active;
	public User()
    {
        super();
    }
	public User(Integer id, String email, String password, String registrationCode, Boolean active) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.registrationCode = registrationCode;
		this.active = active;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRegistrationCode() {
		return registrationCode;
	}
	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
