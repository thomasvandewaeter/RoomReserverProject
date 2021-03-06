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
import interceptor.ReserverInterceptor;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.interceptor.Interceptors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import sessionbeans.RemoveReservationTimer;
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
    
    @EJB
    RemoveReservationTimer removeReservationTimer;
    
    /**
     * Creates a new instance of ReservationController
     */
    public ReservationController() {
    }
    
    @Interceptors(ReserverInterceptor.class)
    public String startNewReservation(){
        
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
        //ReservationPK rpk = new ReservationPK(user.getId(), selectedRoom.getId());
        //reservation.setReservationPK(rpk);
        
        //Persist entity
        //reservationFacade.create(reservation);
    }
    
    //FINISH THE REGISTRATION
    public String completeReservation(){
        
        //check if there exists a user with given email
        User reguser =  userFacade.checkEmail(user.getEmail());
        
        ReservationPK rpk = new ReservationPK();
        
        //persist new user in database if email is unique
        if(reguser == null){
            
            userFacade.create(user);
            reguser = userFacade.checkEmail(user.getEmail());
            System.out.println("User email " + user.getEmail() + " did not exist yet in dB, persising new user...");
            System.out.println("User id: " + reguser.getId());

        } else{
            System.out.println("User email " + user.getEmail() + " already existed in dB.");
            System.out.println("User id: " + reguser.getId());

        }
        
        rpk.setUserId(reguser.getId());
        rpk.setRoomId(selectedRoom.getId());
        
        reservation.setReservationPK(rpk);
        
        reservationFacade.create(reservation);
        
        removeReservationTimer.startTimer(selectedRoom.getId(),reguser.getId());
        sendConfirmationMail(user.getEmail(), selectedRoom.getId(), reguser.getId());
        
        return "confirmation.xhtml";
    }
    
    public static void sendConfirmationMail(String recipient, int roomId, int userId) { //REMOVE ME todo ##############################################################

        final String username = "beanstestmail@gmail.com";
        final String password = "beanstestmail3000";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            
            String confirmationLink = "http://localhost:8080/RoomReserverProject/rest/reservation/" + roomId + "-" + userId;

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("beanstestmail@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Room Reserver - Confirm your reservation");
            message.setContent("<h1>Please confirm or cancel your reservation: </h1> \n <a href=\""+confirmationLink+"\">Confirm</a>", "text/html");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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
