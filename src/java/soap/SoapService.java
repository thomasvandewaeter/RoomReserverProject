package soap;

import entities.Reservation;
import entities.Room;
import entities.User;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sessionbeans.ReservationFacade;


@WebService
public class SoapService {
    
    @EJB
    private ReservationFacade reservationFacade;
    
    @PersistenceContext(unitName = "RoomReserverProjectPU")
    private EntityManager em;

    @WebMethod(operationName = "getRoomNameById")
    public String getRoomNameById(@WebParam(name = "id") int id) {
        Room room = em.find(Room.class, id);
        return room.getName();
    }

    @WebMethod(operationName = "getUserEmailById")
    public String getUserEmailById(@WebParam(name = "id") int id) {
        User user = em.find(User.class, id);
        return user.getEmail();
    }      
}
