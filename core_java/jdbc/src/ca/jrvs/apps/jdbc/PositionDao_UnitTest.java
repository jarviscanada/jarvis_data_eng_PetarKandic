package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class PositionDao_UnitTest {

    private PositionDao positionDao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private Logger mockLogger;

    @Before
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockLogger = mock(Logger.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        // Simulate finding one result
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        positionDao = new PositionDao(mockConnection);
    }

    @Test
    public void testSaveNewPosition() throws SQLException {
        // Assume Position is properly defined
        Position newPosition = new Position();
        // Set the ID to simulate a new Position that does not exist in DB yet
        newPosition.setId(1);

        // Simulate not finding the position
        when(mockResultSet.next()).thenReturn(false);

        Position result = positionDao.save(newPosition);

        // Verify that an insert happened
        verify(mockStatement, times(1)).executeUpdate();
        assertEquals(newPosition, result);
    }

    @Test
    public void testSaveExistingPosition() throws SQLException {
        Position existingPosition = new Position();
        existingPosition.setId(1);

        when(mockResultSet.next()).thenReturn(true); // Simulate finding the position

        Position result = positionDao.save(existingPosition);

        verify(mockStatement, times(1)).executeUpdate(); // Verify that an update happened
        assertEquals(existingPosition, result);
    }

    @Test
    public void testFindByIdFound() throws SQLException {
        when(mockResultSet.getInt("id")).thenReturn(1);

        Optional<Position> result = positionDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() throws SQLException {
        when(mockResultSet.next()).thenReturn(false); // Simulate not finding the position

        Optional<Position> result = positionDao.findById(1);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindBySymbol() throws SQLException {
        positionDao.findBySymbol("AAPL");

        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testFindAll() throws SQLException {
        positionDao.findAll();

        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testDeleteById() throws SQLException {
        positionDao.deleteById(1);

        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteAll() throws SQLException {
        positionDao.deleteAll();

        verify(mockStatement, times(1)).executeUpdate();
    }

}
