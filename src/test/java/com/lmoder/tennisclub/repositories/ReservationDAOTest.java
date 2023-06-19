package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.Reservation;
import com.lmoder.tennisclub.models.SurfaceType;
import com.lmoder.tennisclub.models.TennisCourt;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReservationDAOTest {

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private ReservationDAO reservationDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationDAO = new ReservationDAO();
        reservationDAO.setCurrentSession(session);
        reservationDAO.setCurrentTransaction(transaction);
    }

    @Test
    void testSave() {
        // Arrange
        Reservation reservation = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), true,
                "123456789", "John Doe", new TennisCourt(SurfaceType.HARD));

        // Act
        reservationDAO.save(reservation);

        // Assert
        verify(reservationDAO.getCurrentSession(), times(1)).persist(reservation);
    }

    @Test
    void testUpdate() {
        // Arrange
        Reservation reservation = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), true,
                "123456789", "John Doe", new TennisCourt(SurfaceType.HARD));

        // Act
        reservationDAO.update(reservation);

        // Assert
        verify(reservationDAO.getCurrentSession(), times(1)).merge(reservation);
    }

    @Test
    void testFindById() {
        // Arrange
        Long reservationId = 1L;
        Reservation expectedReservation = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                true, "123456789", "John Doe", new TennisCourt(SurfaceType.HARD));
        when(reservationDAO.getCurrentSession().get(Reservation.class, reservationId)).thenReturn(expectedReservation);

        // Act
        Reservation reservation = reservationDAO.findById(reservationId);

        // Assert
        assertEquals(expectedReservation, reservation);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                true, "123456789", "John Doe", new TennisCourt(SurfaceType.HARD));
        when(reservationDAO.getCurrentSession().get(Reservation.class, reservationId)).thenReturn(reservation);

        // Act
        reservationDAO.deleteById(reservationId);

        // Assert
        verify(reservationDAO.getCurrentSession(), times(1)).remove(reservation);
    }

    @Test
    void testFindAll() {
        // Arrange
        Reservation reservation1 = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                true, "123456789", "John Doe", new TennisCourt(SurfaceType.HARD));
        Reservation reservation2 = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                true, "987654321", "Jane Smith", new TennisCourt(SurfaceType.HARD));
        List<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(reservation1);
        expectedReservations.add(reservation2);

        Query<Reservation> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Reservation.class))).thenReturn(query);
        when(query.list()).thenReturn(expectedReservations);

        // Act
        List<Reservation> reservations = reservationDAO.findAll();

        // Assert
        assertEquals(expectedReservations, reservations);
        verify(session, times(1)).createQuery(anyString(), eq(Reservation.class));
        verify(query, times(1)).list();
    }
}
