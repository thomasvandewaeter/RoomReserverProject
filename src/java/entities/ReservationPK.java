/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Thomas
 */
@Embeddable
public class ReservationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "room_id")
    private int roomId;

    public ReservationPK() {
    }

    public ReservationPK(int userId, int roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) roomId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationPK)) {
            return false;
        }
        ReservationPK other = (ReservationPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.roomId != other.roomId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ReservationPK[ userId=" + userId + ", roomId=" + roomId + " ]";
    }
    
}
