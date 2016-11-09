package com.realdolmen.domain.beans;

import java.util.PropertyResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * 
 * @author JCPBB69
 * This producer is made in order to get the msgs bundle into CDI beans.
 * Injecting needs to be with transient, e.g.:
 * 		private transient @Inject PropertyResourceBundle bundle;
 */
@RequestScoped
public class BundleProducer {

    @Produces
    public PropertyResourceBundle getBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{msgs}", PropertyResourceBundle.class);
    }

}