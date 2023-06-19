package com.lmoder.tennisclub.services;

import com.lmoder.tennisclub.models.SurfaceType;
import com.lmoder.tennisclub.models.TennisCourt;
import com.lmoder.tennisclub.repositories.TennisCourtDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class TennisCourtServiceTest {

    @Mock
    private TennisCourtDAO tennisCourtDAO;

    private TennisCourtService tennisCourtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tennisCourtService = new TennisCourtService(tennisCourtDAO);
    }

    @Test
    public void testList() {
        // Arrange
        List<TennisCourt> expectedCourts = Arrays.asList(
                new TennisCourt(SurfaceType.HARD),
                new TennisCourt(SurfaceType.CLAY)
        );

        when(tennisCourtDAO.findAll()).thenReturn(expectedCourts);

        // Act
        List<TennisCourt> actualCourts = tennisCourtService.list();

        // Assert
        assertEquals(expectedCourts, actualCourts);
        verify(tennisCourtDAO, times(1)).findAll();
    }

    @Test
    public void testGet() {
        // Arrange
        long courtId = 1L;
        TennisCourt expectedCourt = new TennisCourt(SurfaceType.HARD);

        when(tennisCourtDAO.findById(courtId)).thenReturn(expectedCourt);

        // Act
        Optional<TennisCourt> actualCourt = tennisCourtService.get(courtId);

        // Assert
        assertEquals(Optional.of(expectedCourt), actualCourt);
        verify(tennisCourtDAO, times(1)).findById(courtId);
    }

    @Test
    public void testRemove() {
        // Arrange
        long courtId = 1L;

        // Act
        tennisCourtService.remove(courtId);

        // Assert
        verify(tennisCourtDAO, times(1)).deleteById(courtId);
    }

    @Test
    public void testCreate() {
        // Arrange
        TennisCourt tennisCourt = new TennisCourt(SurfaceType.HARD);

        // Act
        tennisCourtService.create(tennisCourt);

        // Assert
        verify(tennisCourtDAO, times(1)).save(tennisCourt);
    }

    @Test
    public void testUpdate() {
        // Arrange
        TennisCourt tennisCourt = new TennisCourt(SurfaceType.HARD);

        // Act
        tennisCourtService.update(tennisCourt);

        // Assert
        verify(tennisCourtDAO, times(1)).update(tennisCourt);
    }
}
