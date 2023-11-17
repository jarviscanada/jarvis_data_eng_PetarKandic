package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuoteDao_IntTest {

    private DatabaseConnectionManager dbConnectionManager;
    private QuoteDao quoteDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize the connection manager with the test database details
        dbConnectionManager = new DatabaseConnectionManager("localhost:5432", "postgres", "postgres", "password");
        // Create a connection
        connection = dbConnectionManager.getConnection();
        // Initialize the QuoteDao with the test connection
        quoteDao = new QuoteDao(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException
    {
        connection.close();
    }

    @Test
    public void testSaveNewQuote() throws JsonProcessingException {
        // Test saving a new Quote
        String jsonString = "{ \"Global Quote\": {\"01. symbol\": \"AMZN\", \"02. open\": \"142.9700\", \"03. high\": \"143.1200\", \"04. low\": \"141.2183\", \"05. price\": \"142.0800\", \"06. volume\": \"44521658\", \"07. latest trading day\": \"2023-11-08\", \"08. previous close\": \"142.7100\", \"09. change\": \"-0.6300\", \"10. change percent\": \"-0.4415%\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode root = objectMapper.readTree(jsonString);
        //Extract date from the "Global Quote"
        JsonNode value = root.get("Global Quote");
        //Read data into Quote class
        Quote theStock = objectMapper.readValue(value.toString(), Quote.class);
        Quote savedQuote = quoteDao.save(theStock);
        List<Quote> retrievedQuote = quoteDao.findBySymbol("AMZN");

        assertEquals(retrievedQuote.get(0).getSymbol(), "AMZN");
    }

    @Test
    public void testFindAll() throws JsonProcessingException {
        String jsonStringFirst = "{ \"Global Quote\": {\"01. symbol\": \"AMZN\", \"02. open\": \"142.9700\", \"03. high\": \"143.1200\", \"04. low\": \"141.2183\", \"05. price\": \"142.0800\", \"06. volume\": \"44521658\", \"07. latest trading day\": \"2023-11-08\", \"08. previous close\": \"142.7100\", \"09. change\": \"-0.6300\", \"10. change percent\": \"-0.4415%\"}}";
        String jsonStringSecond = "{ \"Global Quote\": {\"01. symbol\": \"MSFT\", \"02. open\": \"142.9700\", \"03. high\": \"143.1200\", \"04. low\": \"141.2183\", \"05. price\": \"142.0800\", \"06. volume\": \"44521658\", \"07. latest trading day\": \"2023-11-08\", \"08. previous close\": \"142.7100\", \"09. change\": \"-0.6300\", \"10. change percent\": \"-0.4415%\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode rootOne = objectMapper.readTree(jsonStringFirst);
        JsonNode rootTwo = objectMapper.readTree(jsonStringSecond);
        //Extract date from the "Global Quote"
        JsonNode valueOne = rootOne.get("Global Quote");
        JsonNode valueTwo = rootTwo.get("Global Quote");
        //Read data into Quote class
        Quote theStockOne = objectMapper.readValue(valueOne.toString(), Quote.class);
        Quote theStockTwo = objectMapper.readValue(valueTwo.toString(), Quote.class);
        Quote savedQuoteOne = quoteDao.save(theStockOne);
        Quote savedQuoteTwo = quoteDao.save(theStockTwo);
        List<Quote> expectedQuotes = new ArrayList<>();
        expectedQuotes.add(savedQuoteOne);
        expectedQuotes.add(savedQuoteTwo);
        List<Quote> quotes = (List<Quote>) quoteDao.findAll();
        assertNotNull(quotes);
        assertEquals(expectedQuotes.get(0).getSymbol(), quotes.get(0).getSymbol());
        assertEquals(expectedQuotes.get(1).getSymbol(), quotes.get(1).getSymbol());
    }
//
//
//
    @Test
    public void testDeleteAll() {
        // Test deleting all Quotes
        quoteDao.deleteAll();
        List<Quote> quotes = (List<Quote>) quoteDao.findAll();

        assertTrue(quotes.isEmpty());
    }
//
    @Test
    public void testUpdateQuote() throws JsonProcessingException, SQLException {
        // Test updating an existing Quote
        String jsonString = "{ \"Global Quote\": {\"01. symbol\": \"MSFT\", \"02. open\": \"142.9700\", \"03. high\": \"143.1200\", \"04. low\": \"141.2183\", \"05. price\": \"142.0800\", \"06. volume\": \"44521658\", \"07. latest trading day\": \"2023-11-08\", \"08. previous close\": \"142.7100\", \"09. change\": \"-0.6300\", \"10. change percent\": \"-0.4415%\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode root = objectMapper.readTree(jsonString);
        //Extract date from the Global Quote
        JsonNode value = root.get("Global Quote");
        //Read data into Quote class
        Quote savedQuote = objectMapper.readValue(value.toString(), Quote.class);

        // Modify the price of the existing Quote
        savedQuote.setPrice(111.0);
        quoteDao.save(savedQuote);
        List<Quote> updatedQuote = quoteDao.findBySymbol(savedQuote.getSymbol());

        assertEquals(111.0, updatedQuote.get(updatedQuote.size()-1).getPrice());
    }

}
