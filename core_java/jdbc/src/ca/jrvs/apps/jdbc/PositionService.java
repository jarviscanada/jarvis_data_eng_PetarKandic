package ca.jrvs.apps.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

/**
 * This class provides for the functionality of positions.
 * In particular, it allows for certain holdings in the Quote to be "bought" or "sold"
 * These are then stored in the Position table (buy), or removed (sell).
 */
public class PositionService
{
    private PositionDao dao;

    /**
     * Empty constructor.
     */
    public PositionService()
    {

    }

    /**
     * Parses properties.txt.
     * Identical to the function in QuoteService.
     * @return the properties scraped from properties.txt.
     */
    public static Properties parseFile()
    {
        InputStream input = null;
        Properties prop = new Properties();
        try
        {
            input = new FileInputStream("C:\\Users\\petar\\IdeaProjects\\JDBC\\src\\main\\resources\\properties.txt");
            // load a properties file
            prop.load(input);
        }
        catch (IOException ioException)
        {
            StockQuoteController.logger.error("Issue reading from file!");
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException ioException)
                {
                    StockQuoteController.logger.error("Error closing file!");
                }
            }
        }
        return prop;
    }

    /**
     * Buys (adds) a stock with given price, and number of shares.
     * @param symbol
     * @param numberOfShares
     * @param price
     * @return a position with this data.
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    public Position buy(String symbol, int numberOfShares, double price) throws IllegalArgumentException, SQLException {
        if (symbol == null || symbol.trim().isEmpty())
        {
            StockQuoteController.logger.error("IleagalArgumentException!");
        }
        if (numberOfShares <= 0)
        {
            StockQuoteController.logger.error("Negative number of shares!");
        }
        if (price <= 0)
        {
            StockQuoteController.logger.error("Invalid Price!");
        }
        // Calculate the total value paid for the shares
        double valuePaid = numberOfShares * price;
        // Create and save the new position
        Position position = new Position();
        position.setSymbol(symbol);
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(valuePaid);
        Properties prop = parseFile();
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                (prop.getProperty("server")+":"+prop.getProperty("port")),
                prop.getProperty("database"),
                prop.getProperty("username"),
                prop.getProperty("password")
        );
        Connection connection = dcm.getConnection();
        dao = new PositionDao(connection);
        return dao.save(position);
    }

    /**
     * Sells (deletes) a position by ID.
     * @param id
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    public void sell(int id) throws IllegalArgumentException, SQLException {
        Properties prop = parseFile();
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                (prop.getProperty("server")+":"+prop.getProperty("port")),
                prop.getProperty("database"),
                prop.getProperty("username"),
                prop.getProperty("password")
        );
        Connection connection = dcm.getConnection();
        dao = new PositionDao(connection);
        Optional<Position> positionOptional = dao.findById(id);

        if (positionOptional.isPresent())
        {
            Position position = positionOptional.get();
            dao.deleteById(id);
        }
        else
        {
            StockQuoteController.logger.error("Position at" + id + "does not exist!");
        }
    }
}