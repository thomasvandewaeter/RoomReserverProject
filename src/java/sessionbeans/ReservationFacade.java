/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Thomas
 */
@Stateless
public class ReservationFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = "RoomReserverProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservationFacade() {
        super(Reservation.class);
    }
    
    public List<Reservation> getAllReservations(){
        List<Reservation> list = em.createNamedQuery("Reservation.findAll").getResultList();
        return list;
    }
    
    public List<Reservation> getReservationsByRoomId(int id){
        List<Reservation> reservations = em.createNamedQuery("Reservation.findByRoomId").setParameter("roomId", id).getResultList();        
        return reservations;
    }
    
    public List<Reservation> getReservationsByStartTime(Date starttime){
        List<Reservation> reservations = em.createNamedQuery("Reservation.findByStartTime").setParameter("startTime", starttime).getResultList();
        return reservations;
    }    
}
