package managedbeans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entities.Room;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import sessionbeans.RoomFacade;

/**
 *
 * @author Thomas
 */
@Named(value = "OverviewController")
@ApplicationScoped
public class OverviewController {
    
    @EJB
    private RoomFacade roomFacade;
    private List<Room> roomList = new ArrayList<>();

    /**
     * Creates a new instance of OverviewController
     */
    public OverviewController() {
    }
    
    public List<Room> getRoomList(){
        roomList = roomFacade.findAll();
        return roomList;
    }
    
}
