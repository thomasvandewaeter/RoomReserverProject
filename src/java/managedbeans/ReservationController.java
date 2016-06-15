/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entities.Reservation;
import entities.ReservationPK;
import entities.Room;
import entities.User;
import enums.RoomType;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import sessionbeans.ReservationFacade;
import sessionbeans.RoomFacade;
import sessionbeans.UserFacade;

/**
 *
 * @author Thomas
 */
@Named(value = "ReservationController")
@SessionScoped
public class ReservationController implements Serializable {
    
    @EJB
    private ReservationFacade reservationFacade;
    
    @EJB
    private RoomFacade roomFacade;
    
    @EJB
    private UserFacade userFacade;
    
    private Reservation reservation;
    private Room prefRoom; //dummy room object to store preferences
    private User user;

    private Room selectedRoom; //actual room
    private List<Room> roomList; //list of all rooms
    private List<Room> filteredRoomList; //filtered list based on requirements
    
    
    /**
     * Creates a new instance of ReservationController
     */
    public ReservationController() {
    }
    
    public String startNewReservation(){
        
        System.out.println("Starting new reservation...");
        
        reservation = new Reservation(); //new reservation
        prefRoom = new Room(); //preferred room settings
        
        selectedRoom = null; //actual existing room
        user = new User();
        
        //Populate list with all rooms (no filter)
        roomList = roomFacade.findAll();
        filteredRoomList = roomList;
        filteredRoomList = null;
       
       return "reservation.xhtml";
    }
    
    public String cancelReservation(){
        
        //Invalidate user session
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        
        System.out.println("Cancelling Reservation...");
        
        return "welcome.xhtml";
    }
    
    //Updates available room list for new preferences/availability
    public void updateFilteredRoomList(){
        
        //Reset filters
        filteredRoomList = new ArrayList<>(roomList);
        
        if(prefRoom.getRoomType() != null && reservation.getStartTime() == null){
            //Get rooms for selected type
            filteredRoomList = roomFacade.getRoomsByType(prefRoom.getRoomType());
            filteredRoomList = null;
        } else if(reservation.getStartTime() != null){
            
            //get all reservations for selected date
            List<Reservation> res = reservationFacade.getReservationsByStartTime(reservation.getStartTime());
            
            //Remove all rooms from list that have reservations for selected date
            res.stream().forEach((c) -> {
                System.out.println(c.getStartTime());
                filteredRoomList.removeIf(p -> p.getId() == c.getRoom().getId());
            });
            
            //Apply roomtype filter if not null
            if(prefRoom.getRoomType() != null){
                filteredRoomList.removeIf(p -> p.getRoomType() != prefRoom.getRoomType());                
            }       
        }
        
        //System.out.println("Amount of rooms for selection: " + filteredRoomList.size());
    }
    
    //Update list for new selected type
    public void roomtypeChange(){
        System.out.println("Radio set to: " + prefRoom.getRoomType());
        
        updateFilteredRoomList();
    }
    
    //Update date for reservation
    public void dateChange(SelectEvent e){
        Date date = (Date) e.getObject();
        reservation.setStartTime(date);
        System.out.println("Reservation date: " + reservation.getStartTime());
        
        updateFilteredRoomList();
    }
    
    //Complete reservation fields when row is selected
    public void onRowSelect(SelectEvent e){
        selectedRoom = (Room) e.getObject();
        System.out.println("Room selected: " + selectedRoom.getName());
        
        reservation.setRoom(selectedRoom);
        reservation.setConfirmed(false);       
        
        
        //add one day to start of reservation to get end
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getStartTime());
        c.add(Calendar.DATE, 1);
        
        reservation.setEndTime(c.getTime());
        
        // !!!!!!!!!!
        ReservationPK rpk = new ReservationPK(user.getId(), selectedRoom.getId());
        reservation.setReservationPK(rpk);
        
        //Persist entity
        reservationFacade.create(reservation);
    }
    
    public void sendReservationConfirmationEmail(){
        System.out.println("*** mail sent to " + user.getEmail() + " ***");
    }
    
    public RoomType[] getRoomTypes(){
        return RoomType.values();
    }
    
    public List<Room> getFilteredRoomList() {
        return filteredRoomList;
    }

    public void setFilteredRoomList(List<Room> filteredRoomList) {
        this.filteredRoomList = filteredRoomList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Room getPrefRoom() {
        return prefRoom;
    }

    public void setPrefRoom(Room prefRoom) {
        this.prefRoom = prefRoom;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
