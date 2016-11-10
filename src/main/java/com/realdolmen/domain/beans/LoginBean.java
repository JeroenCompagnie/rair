package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.PropertyResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.Partner;
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
	
	
	private transient @Inject PropertyResourceBundle bundle;

	
	private User u = null;
	
	private Boolean isUserNull = false;
	
	private String userName;
	
	private String unhashedPassword;
	
	// In order to show the type of user in the browser => see getter
	@SuppressWarnings("unused")
	private String userType;
	
	// In order to check if user is a partner
	@SuppressWarnings("unused")
	private String userIsPartner;
	
	@PostConstruct
	public void init(){
//		System.out.println("HASH: " + this.hashCode());
//		System.err.println("facescontext: " + facesContext);
//		System.err.println("messageBundleName: " + messageBundleName);
//		System.err.println("locale: " + locale);
//		System.err.println("bundle: " + bundle);
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
	
	public User getUser(){
		return u;
	}
		
	public String getUserType() {
		if(u != null){
			String[] split = u.getClass().getName().split("\\.");
			return split[split.length-1];
		}
		return "noType";
	}
	
	public Boolean getUserIsPartner() {
		if(u==null){
			return false;
		}
		return u.getClass() == Partner.class;
	}
	
	public Boolean getUserIsEmployee() {
		if(u==null){
			return false;
		}
		return u.getClass() == Employee.class;
	}
	
	public Boolean getUserIsCustomer() {
		if(u==null){
			return false;
		}
		return u.getClass() == Customer.class;
	}

	public String login(){
		// First check if user exists:
		User tempUser = userRepository.findUser(userName);
		if(tempUser == null){
			 // user doesn't exist!
			System.out.println("Login failed, invalid username");
			
			String message = bundle.getString("loginUsernamePassError");
		    FacesContext.getCurrentInstance().addMessage(null, 
		        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
			return "";
		}
		// Check password:
		if(!tempUser.checkPassword(unhashedPassword)){
			// password doesn't match
			System.out.println("Login failed, invalid passw");
			
			String message = bundle.getString("loginUsernamePassError");
		    FacesContext.getCurrentInstance().addMessage(null, 
		        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
			return "";
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
