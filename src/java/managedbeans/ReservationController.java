/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entities.Reservation;
import enums.RoomType;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import sessionbeans.ReservationFacade;

/**
 *
 * @author Thomas
 */
@Named(value = "ReservationController")
@SessionScoped
public class ReservationController implements Serializable {
    
    @EJB
    private ReservationFacade reservationFacade;
    

    /**
     * Creates a new instance of ReservationController
     */
    public ReservationController() {
    }
    
    public RoomType[] getRoomTypes(){
        return RoomType.values();
    }
    
}
