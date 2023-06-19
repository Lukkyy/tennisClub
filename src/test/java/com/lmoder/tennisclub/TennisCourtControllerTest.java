package com.lmoder.tennisclub;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmoder.tennisclub.models.SurfaceType;
import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.services.TennisCourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TennisCourtControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TennisCourtService tennisCourtService;

    @InjectMocks
    private TennisCourtController tennisCourtController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(tennisCourtController).build();
    }

    @Test
    public void testGetAllTennisCourts() throws Exception {
        TennisCourt tennisCourt1 = new TennisCourt(1L, SurfaceType.HARD);
        TennisCourt tennisCourt2 = new TennisCourt(2L, SurfaceType.CLAY);
        List<TennisCourt> tennisCourtList = Arrays.asList(tennisCourt1, tennisCourt2);

        when(tennisCourtService.list()).thenReturn(tennisCourtList);

        mockMvc.perform(get("/api/court"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courtId").value(1))
                .andExpect(jsonPath("$[0].surfaceType").value("HARD"))
                .andExpect(jsonPath("$[1].courtId").value(2))
                .andExpect(jsonPath("$[1].surfaceType").value("CLAY"));

        verify(tennisCourtService, times(1)).list();
    }

    @Test
    public void testGetTennisCourt() throws Exception {
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.HARD);

        when(tennisCourtService.get(anyLong())).thenReturn(Optional.of(tennisCourt));

        MvcResult result1 = mockMvc.perform(get("/api/court/{courtid}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courtId").value(1))
                .andExpect(jsonPath("$.surfaceType").value("HARD")).andReturn();

        verify(tennisCourtService, times(1)).get(anyLong());
    }

    @Test
    public void testDeleteCourt() throws Exception {
        mockMvc.perform(delete("/api/court/{courtid}", 1L))
                .andExpect(status().isOk());

        verify(tennisCourtService, times(1)).remove(anyLong());
    }

    @Test
    public void testSaveCourt() throws Exception {
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.HARD);

        // Mock the behavior of the create method
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                TennisCourt createdCourt = invocation.getArgument(0);
                createdCourt.setCourtId(1L); // Set the courtId to 1L
                return null;
            }
        }).when(tennisCourtService).create(any(TennisCourt.class));

        String requestBody = new ObjectMapper().writeValueAsString(tennisCourt);

        MvcResult result = mockMvc.perform(post("/api/court")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        long courtId = Long.parseLong(responseContent);

        assertEquals(1L, courtId);

        verify(tennisCourtService, times(1)).create(any(TennisCourt.class));
    }

    @Test
    public void testUpdate() throws Exception {
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.CLAY);

        mockMvc.perform(put("/api/court")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courtId\": 1, \"surfaceType\":\"CLAY\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courtId").value(1))
                .andExpect(jsonPath("$.surfaceType").value("CLAY"));

        verify(tennisCourtService, times(1)).update(any(TennisCourt.class));
    }
}
