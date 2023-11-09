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

public class QuoteDao_UnitTest {

    private QuoteDao quoteDao;
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

        quoteDao = new QuoteDao(mockConnection);
    }

    @Test
    public void testSaveNewQuote() throws SQLException {
        // Assume Quote is a properly defined bean with its fields and getters/setters
        Quote newQuote = new Quote();
        // Set the ID to simulate a new Quote that does not exist in DB yet
        newQuote.setId(1);

        // Simulate not finding the quote
        when(mockResultSet.next()).thenReturn(false);

        Quote result = quoteDao.save(newQuote);

        // Verify that an insert happened
        verify(mockStatement, times(1)).executeUpdate();
        assertEquals(newQuote, result);
    }

    @Test
    public void testSaveExistingQuote() throws SQLException {
        Quote existingQuote = new Quote();
        existingQuote.setId(1);

        // Simulate finding the quote
        when(mockResultSet.next()).thenReturn(true);

        Quote result = quoteDao.save(existingQuote);

        // Verify that an update happened
        verify(mockStatement, times(1)).executeUpdate();
        assertEquals(existingQuote, result);
    }

    @Test
    public void testFindByIdFound() throws SQLException {
        when(mockResultSet.getInt("id")).thenReturn(1);

        Optional<Quote> result = quoteDao.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testFindByIdNotFound() throws SQLException {
        // Simulate not finding the quote
        when(mockResultSet.next()).thenReturn(false);

        Optional<Quote> result = quoteDao.findById(1);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAll() throws SQLException {
        quoteDao.findAll();

        verify(mockStatement, times(1)).executeQuery();
    }

    @Test
    public void testDeleteById() throws SQLException {
        quoteDao.deleteById(1);

        verify(mockStatement, times(1)).executeUpdate();
    }

}
