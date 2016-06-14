/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Room;
import entities.User;
import enums.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Thomas
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "RoomReserverProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User checkEmail(String email){
        List<User> users = em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
        if(users.isEmpty()){
            System.out.println("*** No user with EMAIL " + email + " found in database ***");
            return null;
        }else {
            System.out.println("*** User with EMAIL " + users.get(0).getEmail() + " found in database, has name " + users.get(0).getFirstname() + " ***");
            return users.get(0);
        }
    }
    
    
}
