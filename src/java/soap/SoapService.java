package soap;

import entities.Room;
import entities.User;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@WebService
public class SoapService {
    
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
