package com.lmoder.tennisclub.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lmoder.tennisclub.models.Reservation;
import com.lmoder.tennisclub.models.SurfaceType;
import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.repositories.CustomerDAO;
import com.lmoder.tennisclub.repositories.ReservationDAO;
import com.lmoder.tennisclub.repositories.TennisCourtDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class ReservationServiceTest {

    @Mock
    private ReservationDAO reservationDAO;

    @Mock
    private TennisCourtDAO tennisCourtDAO;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testList() {
        // Arrange
        List<Reservation> expectedReservations = new ArrayList<>();
        when(reservationDAO.findAll()).thenReturn(expectedReservations);

        // Act
        List<Reservation> reservations = reservationService.list();

        // Assert
        assertEquals(expectedReservations, reservations);
        verify(reservationDAO, times(1)).findAll();
    }

    @Test
    void testGet() {
        // Arrange
        long reservationId = 1L;
        Reservation expectedReservation = new Reservation();
        when(reservationDAO.findById(reservationId)).thenReturn(expectedReservation);

        // Act
        Optional<Reservation> reservation = reservationService.get(reservationId);

        // Assert
        assertEquals(Optional.of(expectedReservation), reservation);
        verify(reservationDAO, times(1)).findById(reservationId);
    }

    @Test
    void testGetByPhoneNumber() {
        // Arrange
        String phoneNumber = "123456789";
        boolean inFuture = true;

        // Create a list of reservations with some matching the phone number and future criteria
        List<Reservation> allReservations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureTime = now.plusHours(1);
        LocalDateTime pastTime = now.minusHours(1);

        allReservations.add(new Reservation(futureTime, futureTime.plusHours(1), true, phoneNumber, "Jane Smith", new TennisCourt()));
        allReservations.add(new Reservation(pastTime, pastTime.plusHours(1), true, phoneNumber, "Jane Smith", new TennisCourt()));
        allReservations.add(new Reservation(futureTime.plusHours(2), futureTime.plusHours(3), true, "987654321", "John Doe", new TennisCourt()));
        allReservations.add(new Reservation(futureTime, futureTime.plusHours(1), false, phoneNumber, "Jane Smith", new TennisCourt()));

        when(reservationDAO.findAll()).thenReturn(allReservations);

        // Act
        List<Reservation> reservations = reservationService.getByPhoneNumber(phoneNumber, inFuture);

        // Filter the reservations based on the provided phone number and future criteria
        List<Reservation> expectedReservations = allReservations.stream()
                .filter(r -> r.getPhoneNumber().equals(phoneNumber) && r.getStartDate().isAfter(now))
                .collect(Collectors.toList());

        // Assert
        assertEquals(expectedReservations, reservations);
        verify(reservationDAO, times(1)).findAll();
    }



    @Test
    void testCreate_ValidReservation() {
        // Arrange
        long courtId = 1L;
        String phoneNumber = "123456789";
        String customerName = "John Doe";
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.HARD);
        Reservation newReservation = new Reservation(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), true, phoneNumber, customerName, tennisCourt);

        //Mock that court is already in database
        when(tennisCourtDAO.findById(courtId)).thenReturn(tennisCourt);
        // Mock the save method to perform the verification
        doAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            assertEquals(newReservation, arg); // Verify that the correct reservation object is passed to the save method
            return null;
        }).when(reservationDAO).save(newReservation);

        // Act
        double price = reservationService.create(newReservation);

        // Assert
        assertEquals(tennisCourt.getSurfaceType().getPrice() * 60.0, price); // Assuming the duration is 2 hours and rent price is 10.0 per hour for a doubles game
        verify(reservationDAO, times(1)).save(newReservation);
    }


    @Test
    void testCreate_OverlappingReservation() {
        // Arrange
        long courtId = 1L;
        String phoneNumber = "123456789";
        String customerName = "John Doe";
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.HARD);
        Reservation existingReservation = new Reservation(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3), true, "987654321", "Jane Smith", tennisCourt);
        Reservation newReservation = new Reservation(LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(4), false, phoneNumber, customerName, tennisCourt);

        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(existingReservation);
        when(reservationDAO.findAll()).thenReturn(existingReservations);
        //Mock that court is already in database
        when(tennisCourtDAO.findById(courtId)).thenReturn(tennisCourt);

        // Act
        double price = reservationService.create(newReservation);

        // Assert
        assertEquals(2.0, price); // Assuming the duration is 2 hours and rent price is 10.0 per hour for a singles game
        verify(reservationDAO, times(1)).findAll();
        verify(reservationDAO, never()).save(newReservation);
    }

    @Test
    void testCreate_InvalidTimeRange() {
        // Arrange
        long courtId = 1L;
        String phoneNumber = "123456789";
        String customerName = "John Doe";
        TennisCourt tennisCourt = new TennisCourt(SurfaceType.HARD);
        Reservation newReservation = new Reservation(LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(1), true, phoneNumber, customerName, tennisCourt);

        // Act
        double price = reservationService.create(newReservation);

        // Assert
        assertEquals(1.0, price); // Invalid time range should return 1.0 (arbitrary value)
        verify(reservationDAO, never()).findAll();
        verify(reservationDAO, never()).save(newReservation);
    }

    @Test
    void testRemove() {
        // Arrange
        long reservationId = 1L;

        // Act
        reservationService.remove(reservationId);

        // Assert
        verify(reservationDAO, times(1)).deleteById(reservationId);
    }

    @Test
    void testUpdate() {
        // Arrange
        Reservation reservation = new Reservation();

        // Act
        reservationService.update(reservation);

        // Assert
        verify(reservationDAO, times(1)).update(reservation);
    }
}
