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

public class PositionDao_IntTest {

    private DatabaseConnectionManager dbConnectionManager;
    private PositionDao positionDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        dbConnectionManager = new DatabaseConnectionManager("localhost:5432", "postgres", "postgres", "password");
        // Create a connection
        connection = dbConnectionManager.getConnection();
        // Initialize the PositionDao with the test connection
        positionDao = new PositionDao(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException
    {
        connection.close();
    }

    @Test
    public void testSaveNewPosition() throws JsonProcessingException
    {
        // Test saving a new Position
        String jsonString = "{ \"Test Int\": {\"id\": \"1\", \"symbol\": \"NVDA\", \"numOfShares\": \"142\", \"valuePaid\": \"143.1200\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode root = objectMapper.readTree(jsonString);
        JsonNode value = root.get("Test Int");
        Position thePosition = objectMapper.readValue(value.toString(), Position.class);
        Position savedPosition = positionDao.save(thePosition);
        List<Position> retrievedPosition = positionDao.findBySymbol("NVDA");

        assertEquals(retrievedPosition.get(0).getSymbol(), "NVDA");
        //assertEquals(theStock, retrievedQuote.get(0));
    }

    @Test
    public void testFindPosition() throws JsonProcessingException {
        // Test finding all Positions
        String jsonStringFirst = "{ \"Test Int\": {\"id\": \"1\", \"symbol\": \"NVDA\", \"numOfShares\": \"142\", \"valuePaid\": \"143.1200\"}}";
        String jsonStringSecond = "{ \"Test Int\": {\"id\": \"1\", \"symbol\": \"NVDA\", \"numOfShares\": \"546\", \"valuePaid\": \"200.1200\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode rootOne = objectMapper.readTree(jsonStringFirst);
        JsonNode rootTwo = objectMapper.readTree(jsonStringSecond);
        //Extract date from the Global Position
        JsonNode valueOne = rootOne.get("Test Int");
        JsonNode valueTwo = rootTwo.get("Test Int");
        //Read data into Position class
        Position thePositionOne = objectMapper.readValue(valueOne.toString(), Position.class);
        Position thePositionTwo = objectMapper.readValue(valueTwo.toString(), Position.class);
        Position savedPositionOne = positionDao.save(thePositionOne);
        Position savedPositionTwo = positionDao.save(thePositionTwo);
        // Assume expectedPosition is a predefined list of quotes that should be in the test DB
        List<Position> expectedPosition = new ArrayList<>();
        expectedPosition.add(savedPositionOne);
        expectedPosition.add(savedPositionTwo);
        List<Position> position = (List<Position>) positionDao.findAll();
        assertNotNull(position);
        assertEquals(expectedPosition.get(0).getSymbol(), position.get(0).getSymbol());
        assertEquals(expectedPosition.get(1).getSymbol(), position.get(1).getSymbol());
    }

    @Test
    public void testDeleteAll() {
        // Test deleting all Positions
        positionDao.deleteAll();
        List<Position> quotes = (List<Position>) positionDao.findAll();

        assertTrue(quotes.isEmpty());
    }

    @Test
    public void testUpdatePosition() throws JsonProcessingException, SQLException {
        // Test updating an existing Position
        String jsonString = "{ \"Test Int\": {\"id\": \"1\", \"symbol\": \"NVDA\", \"numOfShares\": \"142\", \"valuePaid\": \"143.1200\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonNode root = objectMapper.readTree(jsonString);
        //Extract date from the Global Position
        JsonNode value = root.get("Test Int");
        //Read data into Position class
        Position savedPosition = objectMapper.readValue(value.toString(), Position.class);

        savedPosition.setValuePaid(111.0);
        positionDao.save(savedPosition);
        List<Position> updatedPosition = positionDao.findBySymbol(savedPosition.getSymbol());

        assertEquals(111.0, updatedPosition.get(updatedPosition.size()-1).getValuePaid());
    }
}
