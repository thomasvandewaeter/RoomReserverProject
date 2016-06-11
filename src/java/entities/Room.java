/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "room")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Room.findAll", query = "SELECT r FROM Room r"),
    @NamedQuery(name = "Room.findById", query = "SELECT r FROM Room r WHERE r.id = :id"),
    @NamedQuery(name = "Room.findByName", query = "SELECT r FROM Room r WHERE r.name = :name"),
    @NamedQuery(name = "Room.findByAddress", query = "SELECT r FROM Room r WHERE r.address = :address"),
    @NamedQuery(name = "Room.findBySeats", query = "SELECT r FROM Room r WHERE r.seats = :seats"),
    @NamedQuery(name = "Room.findByRoomType", query = "SELECT r FROM Room r WHERE r.roomType = :roomType"),
    @NamedQuery(name = "Room.findByCushionedChairs", query = "SELECT r FROM Room r WHERE r.cushionedChairs = :cushionedChairs")})
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seats")
    private int seats;
    @Basic(optional = false)
    @NotNull
    @Column(name = "room_type")
    private int roomType;
    @Column(name = "cushionedChairs")
    private Boolean cushionedChairs;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private Collection<Reservation> reservationCollection;

    public Room() {
    }

    public Room(Integer id) {
        this.id = id;
    }

    public Room(Integer id, String name, String address, int seats, int roomType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.seats = seats;
        this.roomType = roomType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public Boolean getCushionedChairs() {
        return cushionedChairs;
    }

    public void setCushionedChairs(Boolean cushionedChairs) {
        this.cushionedChairs = cushionedChairs;
    }

    @XmlTransient
    public Collection<Reservation> getReservationCollection() {
        return reservationCollection;
    }

    public void setReservationCollection(Collection<Reservation> reservationCollection) {
        this.reservationCollection = reservationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Room[ id=" + id + " ]";
    }
    
}
