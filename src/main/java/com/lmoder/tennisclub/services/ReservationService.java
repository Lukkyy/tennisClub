package com.lmoder.tennisclub.services;

import com.lmoder.tennisclub.models.Customer;
import com.lmoder.tennisclub.models.Reservation;
import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.repositories.CustomerDAO;
import com.lmoder.tennisclub.repositories.ReservationDAO;
import com.lmoder.tennisclub.repositories.TennisCourtDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReservationService {
    @Autowired
    private ReservationDAO reservationRepository;
    @Autowired
    private CustomerDAO customerRepository;
    @Autowired
    private TennisCourtDAO courtRepository;

    public ReservationService(ReservationDAO reservationRepository, CustomerDAO customerRepository,
                              TennisCourtDAO tennisCourtRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.courtRepository = tennisCourtRepository;
    }

    public List<Reservation> list(){
        reservationRepository.openCurrentSession();
        var reservations = reservationRepository.findAll();
        reservationRepository.closeCurrentSession();
        return reservations;
    }

    public Optional<Reservation> get(long reservationId) {
        reservationRepository.openCurrentSession();
        var reservation =  Optional.ofNullable(reservationRepository.findById(reservationId));
        reservationRepository.closeCurrentSession();
        return reservation;
    }

    public List<Reservation> getByCourtId(long courtId) {
        List<Reservation> reservationList = list();
        return reservationList.stream()
                .filter(res -> res.getTennisCourt().getCourtId() == courtId)
                .sorted(Comparator.comparing(Reservation::getCreated))
                .collect(Collectors.toList());
    }

    public List<Reservation> getByPhoneNumber(String phoneNumber, boolean inFuture) {
        List<Reservation> reservationList = list();

        Stream<Reservation> reservationStream = reservationList.stream()
                .filter(res -> res.getPhoneNumber().equals(phoneNumber));
        if (inFuture) {
            reservationStream = reservationStream.filter(res -> res.getStartDate().isAfter(LocalDateTime.now()));
        }

        return reservationStream.collect(Collectors.toList());
    }

    public double create(Reservation newRes) {
        if (!newRes.getStartDate().isBefore(newRes.getEndDate())){
            return 1;
        }


        courtRepository.openCurrentSession();
        TennisCourt court = courtRepository.findById(newRes.getTennisCourt().getCourtId());
        if (court == null || court.isDeleted()){
            courtRepository.closeCurrentSession();
            return 1;
        }
        courtRepository.closeCurrentSession();

        List<Reservation> courtReservations = getByCourtId(newRes.getTennisCourt().getCourtId());

        var s1 = newRes.getStartDate();
        var e1 = newRes.getEndDate();

        for (Reservation res:courtReservations
             ) {
            var s2 = res.getStartDate();
            var e2 = res.getEndDate();
            if (s1.isBefore(s2) && e1.isAfter(s2) ||
                    s1.isBefore(e2) && e1.isAfter(e2) ||
                    s1.isBefore(s2) && e1.isAfter(e2) ||
                    s1.isAfter(s2) && e1.isBefore(e2) ||
                    s1.isEqual(s2) || e1.isEqual(e2)){
                return 2;
            }
        }
        if (!checkAndAddCustomer(newRes)){
            return 3;
        }
        newRes.setCreated(LocalDateTime.now());

        reservationRepository.openCurrentSessionWithTransaction();
        reservationRepository.save(newRes);
        reservationRepository.closeCurrentSessionwithTransaction();

        double price = Duration.between(s1,e1).toMinutes() * newRes.getTennisCourt().getSurfaceType().getPrice();
        return newRes.isDoublesGame() ? price : 1.5 * price;
    }

    private boolean checkAndAddCustomer(Reservation newRes) {
        customerRepository.openCurrentSession();
        List<Customer> customers = customerRepository.findAll()
                .stream()
                .filter(customer -> Objects.equals(customer.getPhoneNumber(), newRes.getPhoneNumber()))
                .toList();
        customerRepository.closeCurrentSession();

        if (customers.isEmpty()){

            customerRepository.openCurrentSessionWithTransaction();
            customerRepository.save(new Customer(newRes.getPhoneNumber(), newRes.getCustomerName()));
            customerRepository.closeCurrentSessionwithTransaction();

            return true;
        }

        return Objects.equals(newRes.getCustomerName(), customers.get(0).getCustomerName());
    }

    public void remove(long reservationId) {
        reservationRepository.openCurrentSessionWithTransaction();
        reservationRepository.deleteById(reservationId);
        reservationRepository.closeCurrentSessionwithTransaction();
    }

    public void update(Reservation reservation) {
        reservationRepository.openCurrentSessionWithTransaction();
        reservationRepository.update(reservation);
        reservationRepository.closeCurrentSessionwithTransaction();
    }
}
