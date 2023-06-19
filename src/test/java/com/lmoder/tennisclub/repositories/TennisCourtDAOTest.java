package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.SurfaceType;
import com.lmoder.tennisclub.models.TennisCourt;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TennisCourtDAOTest {

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    private TennisCourtDAO tennisCourtDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tennisCourtDAO = new TennisCourtDAO();
        tennisCourtDAO.setCurrentSession(session);
        tennisCourtDAO.setCurrentTransaction(transaction);
    }

    @Test
    public void testSave() {
        // Arrange
        TennisCourt tennisCourt = new TennisCourt(SurfaceType.HARD);

        // Act
        tennisCourtDAO.save(tennisCourt);

        // Assert
        verify(session, times(1)).persist(tennisCourt);
    }

    @Test
    public void testUpdate() {
        // Arrange
        TennisCourt tennisCourt = new TennisCourt(SurfaceType.HARD);

        // Act
        tennisCourtDAO.update(tennisCourt);

        // Assert
        verify(session, times(1)).merge(tennisCourt);
    }

    @Test
    public void testFindById() {
        // Arrange
        long courtId = 1L;
        TennisCourt expectedCourt = new TennisCourt(SurfaceType.HARD);

        when(session.get(TennisCourt.class, courtId)).thenReturn(expectedCourt);

        // Act
        TennisCourt actualCourt = tennisCourtDAO.findById(courtId);

        // Assert
        assertEquals(expectedCourt, actualCourt);
        verify(session, times(1)).get(TennisCourt.class, courtId);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        long courtId = 1L;
        TennisCourt tennisCourt = new TennisCourt(1L, SurfaceType.HARD);

        when(session.get(TennisCourt.class, courtId)).thenReturn(tennisCourt);

        // Act
        tennisCourtDAO.deleteById(courtId);

        // Assert
        verify(session, times(1)).remove(tennisCourt);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<TennisCourt> expectedCourts = Arrays.asList(
                new TennisCourt(SurfaceType.HARD),
                new TennisCourt(SurfaceType.CLAY)
        );

        Query<TennisCourt> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(TennisCourt.class))).thenReturn(query);
        when(query.list()).thenReturn(expectedCourts);

        // Act
        List<TennisCourt> actualCourts = tennisCourtDAO.findAll();

        // Assert
        assertEquals(expectedCourts, actualCourts);
        verify(session, times(1)).createQuery(anyString(), eq(TennisCourt.class));
        verify(query, times(1)).list();
    }
}

