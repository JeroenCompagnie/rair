package com.realdolmen.domain.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.realdolmen.domain.user.User;
import com.realdolmen.repository.UserRepository;

@Named
@SessionScoped
public class LoginBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3191611829476459272L;

	@EJB
    UserRepository userRepository;
	
	private User u = null;
	
	private Boolean isUserNull = false;
	
	private String userName;
	
	private String unhashedPassword;
	
	@PostConstruct
	public void init(){
		System.out.println("HASH: " + this.hashCode());
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUnhashedPassword() {
		return unhashedPassword;
	}

	public void setUnhashedPassword(String unhashedPassword) {
		this.unhashedPassword = unhashedPassword;
	}
	
	public String login(){
		// First check if user exists:
		User tempUser = userRepository.findCustomer(userName);
		if(tempUser == null){
			 // user doesn't exist!
			System.out.println("Login failed, invalid username");
			return ""; // TODO: show error message
		}
		// Check password:
		if(!tempUser.checkPassword(unhashedPassword)){
			// password doesn't match
			System.out.println("Login failed, invalid passw");
			return ""; // TODO: show error message
		}
		
		// Login succeeds:
		u = tempUser;
		System.out.println("Login successful");
		return "";
	}
	
	public boolean getIsUserNull(){
		if(u==null){
			isUserNull = true;
		}
		else{
			isUserNull = false;
		}
		return isUserNull;
	}
	
	public String logout(){
		u = null;
		userName = null;
		unhashedPassword = null;
		System.out.println("Logged out");
		return "index.xhtml";
	}
	
}
