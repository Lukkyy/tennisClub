package com.lmoder.tennisclub;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lmoder.tennisclub.models.Reservation;
import com.lmoder.tennisclub.models.SurfaceType;
import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.services.ReservationService;
import com.lmoder.tennisclub.services.TennisCourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ReservationsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @Mock
    private TennisCourtService tennisCourtService;

    @InjectMocks
    private ReservationsController reservationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationsController).build();
    }

    @Test
    void testGetAllReservations() throws Exception {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.list()).thenReturn(reservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(reservations.size()));

        verify(reservationService, times(1)).list();
    }

    @Test
    void testGetReservation() throws Exception {
        long reservationId = 1L;
        Reservation reservation = new Reservation();

        when(reservationService.get(reservationId)).thenReturn(Optional.of(reservation));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/{reservationid}", reservationId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());

        verify(reservationService, times(1)).get(reservationId);
    }

    @Test
    void testGetReservationsByCourt() throws Exception {
        long courtId = 1L;
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.getByCourtId(courtId)).thenReturn(reservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/court/{courtid}", courtId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(reservations.size()));

        verify(reservationService, times(1)).getByCourtId(courtId);
    }

    @Test
    void testGetReservationsByPhoneNumber() throws Exception {
        String phoneNumber = "123456789";
        boolean inFuture = true;
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.getByPhoneNumber(phoneNumber, inFuture)).thenReturn(reservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservations/phone/{phonenumber}", phoneNumber)
                        .param("inFuture", String.valueOf(inFuture)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(reservations.size()));

        verify(reservationService, times(1)).getByPhoneNumber(phoneNumber, inFuture);
    }

    @Test
    void testCreateReservation() throws Exception {
        String phoneNumber = "123456789";
        String customerName = "John Doe";
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.HARD);
        Reservation reservation = new Reservation(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), true, phoneNumber, customerName, tennisCourt);
        double price = 600;

        when(tennisCourtService.get(tennisCourt.getCourtId())).thenReturn(Optional.of(tennisCourt));
        when(reservationService.create(reservation)).thenReturn(price);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reservation)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteReservation() throws Exception {
        long reservationId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations/{reservationid}", reservationId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(reservationService, times(1)).remove(reservationId);
    }

    @Test
    void testUpdateReservation() throws Exception {
        Reservation reservation = new Reservation();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reservation)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationId").value(reservation.getReservationId()));
    }

    // Helper method to convert an object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
