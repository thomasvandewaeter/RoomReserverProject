/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import sessionbeans.ReservationFacade;

/**
 *
 * @author Thomas
 */
@Named(value = "RegistrationController")
@RequestScoped
public class RegistrationController {
    
    @EJB
    ReservationFacade reservationFacade;

    /**
     * Creates a new instance of RegistrationController
     */
    public RegistrationController() {
    }
    
}
