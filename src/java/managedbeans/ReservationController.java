/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import entities.Reservation;
import entities.Room;
import entities.User;
import enums.RoomType;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import sessionbeans.ReservationFacade;
import sessionbeans.RoomFacade;

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
    
    private Reservation reservation;
    private Room prefRoom;
    private User user;
    private String email = "";
    private String lastname = "";
    private String firstname = "";

    private Room selectedRoom;
    private List<Room> roomList;
    
    /*******************************/
    private String test;
    private RoomType type;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        System.out.println("getFirstname");
        return firstname;
    }

    public void setFirstname(String firstname) {
        System.out.println(firstname + " blablabla tetten");
        this.firstname = firstname;
    }
    
    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
    /*********************************/

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
    
    /**
     * Creates a new instance of ReservationController
     */
    public ReservationController() {
    }
    
    public String startNewReservation(){
        
        System.out.println("Starting new reservation...");
        
        reservation = new Reservation();
        prefRoom = new Room(); //preferred room settings
        
        selectedRoom = new Room(); //actual existing room
        user = new User();
        
        //Populate list with all rooms
        roomList = roomFacade.findAll();
        
        /*
        user.setFirstname("Thomas");
        user.setLastname("Vandewaeter");
        user.setEmail("thomasvandewaeter@hotmail.com");
        */
        
        /*****************************/
        Random random = new Random();
        int i = random.nextInt();        
        test = Integer.toString(i);
        /******************************/
       
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
    
    public void updateRoomList(){
        System.out.println("Radio set to: " + prefRoom.getRoomType());
        roomList = roomFacade.getRoomsByType(prefRoom.getRoomType());
        System.out.println("Amount of rooms for selection: " + roomList.size());
    }
    
    public void testcheck(){
        System.out.println("testcheck");
    }
    
    public RoomType[] getRoomTypes(){
        return RoomType.values();
    }
    
    
}
