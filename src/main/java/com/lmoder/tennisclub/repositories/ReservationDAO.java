package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDAO extends HibernateDAO<Reservation>{
    public ReservationDAO(){
        setClazz(Reservation.class);
    }
}
