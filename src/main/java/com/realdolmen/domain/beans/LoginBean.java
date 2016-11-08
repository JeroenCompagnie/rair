package com.realdolmen.domain.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import com.realdolmen.domain.user.User;
import com.realdolmen.repository.UserRepository;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3191611829476459272L;

	@EJB
    UserRepository userRepository;
	
	private User u = null;
	
	private String userName;
	
	private String unhashedPassword;

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
			return ""; // TODO: show error message
		}
		// Check password:
		if(!tempUser.checkPassword(unhashedPassword)){
			// password doesn't match
			return ""; // TODO: show error message
		}
		
		// Login succeeds:
		u = tempUser;
		System.out.println("Login successful");
		return "";
	}
	
	public boolean isUserNull(){
		return u == null;
	}
	
}
