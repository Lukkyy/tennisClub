package com.lmoder.tennisclub.models;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "RESERVATIONS")
@SQLDelete(sql = "UPDATE RESERVATIONS SET deleted = true WHERE reservationId=?")
@Where(clause = "deleted=false")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "doubles_game")
    private boolean doublesGame;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "customer_name")
    private String customerName;
    private boolean deleted = Boolean.FALSE;
    @ManyToOne
    @JoinColumn(name = "COURTID")
    private TennisCourt tennisCourt;

    public Reservation() {
    }

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, boolean doublesGame, String phoneNumber,
                       String customerName, TennisCourt tennisCourt) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.created = LocalDateTime.now();
        this.doublesGame = doublesGame;
        this.phoneNumber = phoneNumber;
        this.customerName = customerName;
        this.tennisCourt = tennisCourt;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isDoublesGame() {
        return doublesGame;
    }

    public void setDoublesGame(boolean doublesGame) {
        this.doublesGame = doublesGame;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public TennisCourt getTennisCourt() {
        return tennisCourt;
    }

    public void setTennisCourt(TennisCourt tennisCourt) {
        this.tennisCourt = tennisCourt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
