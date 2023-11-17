package ca.jrvs.apps.jdbc;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the control layer of the application
 * Includes the main method
 */
public class StockQuoteController
{
    static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);
    private QuoteService quoteService;
    private PositionService positionService;

    /**
     * User interface for our application.
     * Parses command line arguments.
     */
    public void initClient(String [] args) throws SQLException
    {
        quoteService = new QuoteService();
        positionService = new PositionService();
        if (args[1].equalsIgnoreCase("save") ||
                args[1].equalsIgnoreCase("find") ||
                args[1].equalsIgnoreCase("deleteall"))
        {
            quoteService.getData(args[0], args[1]);
        }
        else if (args[2].equalsIgnoreCase("buy"))
        {
            positionService.buy(args[0], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        }
        else if (args[2].equalsIgnoreCase("sell"))
        {
            positionService.sell(Integer.parseInt(args[3]));
        }
    }

    /**
     * The main method.
     * Accepts user arguments, sends them to initClient.
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException
    {
        if (args.length > 4 || args.length <= 0)
        {
            StockQuoteController.logger.error("Usage: [symbol] [save/find/deleteall/buy/sell] [price] [shares]");
        }
        StockQuoteController stockQuoteController = new StockQuoteController();
        stockQuoteController.initClient(args);
    }

}
