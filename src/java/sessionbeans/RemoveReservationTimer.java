/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Reservation;
import entities.Room;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@LocalBean
@Startup
public class RemoveReservationTimer {

    @Resource
    private TimerService timerService;

    @PersistenceContext(unitName = "RoomReserverProjectPU")
    private EntityManager em;

    @EJB
    private ReservationFacade reservationFacade;
    
    private final int TIMERMILLIS = 20000;

    public void startTimer(int roomId, int userId) {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(roomId + "-" + userId);
        timerService.createSingleActionTimer(TIMERMILLIS, timerConfig);
        System.out.println("INIT Time, reservationid= " + roomId + "-" + userId + " : " + new Date());
    }

    /*
    @PostConstruct
    private void init() {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("2290");
        timerService.createSingleActionTimer(8000, timerConfig); // after 5 seconds
        System.out.println("INIT Time, reservationid= "+ 2290 + " : " + new Date());
    }
     */
    @Timeout
    public void execute(Timer timer) {
        String roomUserId = (String) timer.getInfo();
        String[] parts = roomUserId.split("-");
        int roomId = Integer.parseInt(parts[0]);
        int userId = Integer.parseInt(parts[1]);
        List<Reservation> reservations = em.createNamedQuery("Reservation.findByRoomId").setParameter("roomId", roomId).getResultList();
        
        for (Reservation r : reservations) {
            if (r.getReservationPK().getUserId() == userId) {
                if (r.getConfirmed() != true) {
                    reservationFacade.remove(r);
                }
            }
        }
    }

}
