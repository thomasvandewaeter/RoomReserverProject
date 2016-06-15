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
import java.util.Properties;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import sessionbeans.RemoveReservationTimer;
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
    
    //todo removeme
   
    @EJB
    RemoveReservationTimer removeReservationTimer;
       
    private Reservation reservation;
    private Room prefRoom;
    private User user;


    private Room selectedRoom;
    private List<Room> roomList;
    
    /*******************************/
    private String test;
    private RoomType type;

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
        
        //REMOVE ME todo ##############################################################
        removeReservationTimer.startTimer(7,1);
        sendConfirmationMail("wvanderschraelen@gmail.com", 7, 1);
        
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
