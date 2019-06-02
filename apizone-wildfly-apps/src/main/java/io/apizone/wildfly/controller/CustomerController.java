
package io.apizone.wildfly.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;
import io.apizone.wildfly.model.Customer;
import io.apizone.wildfly.service.CustomerRegistration;

/**
 * Registers a new Customer
 * <p>
 * <p>
 * The @Model stereotype is a convenience mechanism to make this a
 * request-scoped bean that has an EL name
 */
@Model
public class CustomerController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private CustomerRegistration customerRegistration;

    private Customer newCustomer;

    @Inject
    private Logger log;



    @Produces
    @Named
    public Customer getNewCustomer() {
        return newCustomer;
    }

    public void register() {
        try {
            customerRegistration.register(newCustomer);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            initNewCustomer();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful"));
        }
    }



    public void pdfDownload() {
        log.info("------- Reached to PDF Download!!");
        try {
            customerRegistration.pdfDownload();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PDF Downloaded Successfully!", "PDF Downloaded successfully"));
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "PDF Download unsuccessful"));
        }
    }



    @PostConstruct
    public void initNewCustomer() {
        newCustomer = new Customer();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
