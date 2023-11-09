package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StockQuoteController_UnitTest {

    @Mock
    private QuoteService mockQuoteService;

    @Mock
    private PositionService mockPositionService;

    private StockQuoteController stockQuoteController;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        stockQuoteController = new StockQuoteController();
    }

    @Test
    public void testInitClientSave() throws SQLException {
        String[] args = {"AAPL", "save"};
        stockQuoteController.initClient(args);
        verify(mockQuoteService).getData("AAPL", "save");
    }

    @Test
    public void testInitClientBuy() throws SQLException {
        String[] args = {"AAPL", "buy", "100", "10"};
        stockQuoteController.initClient(args);
        verify(mockPositionService).buy("AAPL", 100, 10);
    }

    @Test
    public void testInitClientSell() throws SQLException {
        String[] args = {"AAPL", "sell", "5"};
        stockQuoteController.initClient(args);
        verify(mockPositionService).sell(5);
    }

    @Test
    public void testInitClientIllegalArgumentException() {
        String[] args = {};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockQuoteController.initClient(args);
        });
        assertTrue(exception.getMessage().contains("Invalid number of arguments"));
    }

    @Test
    public void testMainInvalidArguments() {
        String[] args = {"AAPL", "invalidCommand"};
        final SQLException exception = assertThrows(SQLException.class, () -> StockQuoteController.main(args));
        assertEquals("Invalid command", exception.getMessage());
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        closeable.close();
    }
}
