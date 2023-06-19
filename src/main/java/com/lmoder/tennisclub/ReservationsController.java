package com.lmoder.tennisclub;

import com.lmoder.tennisclub.models.Reservation;
import com.lmoder.tennisclub.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ReservationsController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationsController(ReservationService service){
        this.reservationService = service;
    }
    @GetMapping("api/reservations")
    public List<Reservation> getAllReservations(){
        return reservationService.list();
    }
    @GetMapping("api/reservations/{reservationid}")
    public Optional<Reservation> getReservation(@PathVariable("reservationid") long reservationId){
        return reservationService.get(reservationId);
    }
    @GetMapping("api/reservations/court/{courtid}")
    public List<Reservation> getReservationsByCourt(@PathVariable("courtid") long courtId){
        return reservationService.getByCourtId(courtId);
    }

    @GetMapping("api/reservations/phone/{phonenumber}")
    public List<Reservation> getReservationsByPhoneNumber(@PathVariable("phonenumber") String phoneNumber,
                                                          @RequestParam boolean inFuture){
        return reservationService.getByPhoneNumber(phoneNumber, inFuture);
    }

    @PostMapping("api/reservations")
    private double createReservation(@RequestBody Reservation reservation){
        return reservationService.create(reservation);
    }

    @DeleteMapping("api/reservations/{reservationid}")
    private void deleteReservation(@PathVariable("reservationid") long reservationId)
    {
        reservationService.remove(reservationId);
    }

    @PutMapping("api/reservations")
    private Reservation update(@RequestBody Reservation reservation)
    {
        reservationService.update(reservation);
        return reservation;
    }


}
