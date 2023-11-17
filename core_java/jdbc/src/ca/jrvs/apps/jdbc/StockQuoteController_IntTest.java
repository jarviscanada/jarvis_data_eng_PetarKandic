package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StockQuoteController_IntTest {

    private Connection connection;
    private StockQuoteController stockQuoteController;
    private QuoteService quoteService;

    @BeforeEach
    public void setUp() throws SQLException {
        // Use a separate test database
        String url = "jdbc:postgresql://localhost:5432/postgres";
        // Use test credentials
        connection = DriverManager.getConnection(url, "postgres", "password");

        quoteService = new QuoteService();

        stockQuoteController = new StockQuoteController();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        Statement statement = connection.createStatement();

        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testSaveAndFindMSFTQuote() throws SQLException {
        // Simulate command line input for saving a quote for MSFT
        String[] args = {"MSFT", "save"};
        stockQuoteController.initClient(args);

        // Verify that the quote was saved successfully
        QuoteDao quoteDao = new QuoteDao(connection);
        List<Quote> quotes = quoteDao.findBySymbol("MSFT");
        assertFalse(quotes.isEmpty(), "The quote list should not be empty");

        // Verify that the saved quote is for MSFT
        Quote msftQuote = quotes.get(0);
        assertEquals("MSFT", msftQuote.getSymbol(), "The symbol should be MSFT");
    }

    @Test
    public void testBuyMSFTStockAndCheckPosition() throws SQLException {
        // Set up the quote in the database
        QuoteDao quoteDao = new QuoteDao(connection);
        Quote msftQuote = new Quote();
        msftQuote.setSymbol("MSFT");
        // Assume a price
        msftQuote.setPrice(280.00);
        quoteDao.save(msftQuote);

        // Check if the MSFT quote is present
        List<Quote> quotes = quoteDao.findBySymbol("MSFT");
        assertFalse(quotes.isEmpty(), "The quote list should not be empty before buying");

        // Buy MSFT stock
        PositionDao positionDao = new PositionDao(connection);
        Position newPosition = new Position();
        newPosition.setSymbol("MSFT");
        // Assume buying 10 shares
        newPosition.setNumOfShares(10);
        // Total value paid for the shares
        newPosition.setValuePaid(280.00 * 10);
        positionDao.save(newPosition);

        // Verify that the position was saved successfully
        List<Position> positions = positionDao.findBySymbol("MSFT");
        assertFalse(positions.isEmpty(), "The position list should not be empty after buying");

        // Verify that the saved position is for MSFT and the number of shares is correct
        Position position = positions.get(0);
        assertEquals("MSFT", position.getSymbol(), "The symbol should be MSFT");
        assertEquals(10, position.getNumOfShares(), "The number of shares should be 10");
        assertEquals(2800.00, position.getValuePaid(), "The value paid should be 2800.00");
    }

    @Test
    public void testSellMSFTStockAndCheckRemoval() throws SQLException {
        // Assume a MSFT position is already in the database, insert it for the test
        PositionDao positionDao = new PositionDao(connection);
        Position existingPosition = new Position();
        existingPosition.setSymbol("MSFT");
        existingPosition.setNumOfShares(10);
        existingPosition.setValuePaid(2800.00); // Assume the initial value paid for 10 shares
        Position savedPosition = positionDao.save(existingPosition);

        // Check that the position is present before selling
        Optional<Position> positionBeforeSale = positionDao.findById(savedPosition.getId());
        assertTrue(positionBeforeSale.isPresent(), "Position must exist before selling");

        // Sell the MSFT stock, in the test we simply remove the position from the database
        positionDao.deleteById(savedPosition.getId());

        // Verify that the position has been removed from the database
        Optional<Position> positionAfterSale = positionDao.findById(savedPosition.getId());
        assertFalse(positionAfterSale.isPresent(), "Position should not exist after being sold");
    }

}