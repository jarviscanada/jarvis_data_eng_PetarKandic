package ca.jrvs.apps.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Acts as a service layer
 */
public class QuoteService
{
    /**
     * Parses the properties file.
     * input directory should be updated depending on deployment.
     * @return the object containing the properties.
     */
    public static Properties parseFile()
    {
        InputStream input = null;
        Properties prop = new Properties();
        //trying to find and load the file
        try
        {
            input = new FileInputStream("C:\\Users\\petar\\IdeaProjects\\JDBC\\src\\main\\resources\\properties.txt");
            // load the properties file
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
                    //important
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
     * Creates a connection using DatabaseConnectionManager.
     * Passes this connection to QuoteDao and QuoteHTTPHelper.
     * Parses different commands, and calls appropriate functions in QuoteDao.
     * @param symbol
     * @param command
     */
    public static void getData(String symbol, String command)
    {
        Properties prop = parseFile();
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                (prop.getProperty("server")+":"+prop.getProperty("port")),
                prop.getProperty("database"),
                prop.getProperty("username"),
                prop.getProperty("password")
        );
        try
        {
            Connection connection = dcm.getConnection();

            QuoteDao quoteDao = new QuoteDao(connection);
            QuoteHTTPHelper qhttp = new QuoteHTTPHelper(prop.getProperty("api-key"));
            Quote quoteClass = qhttp.fetchQuoteInfo(symbol);
            if (command.equalsIgnoreCase("save"))
            {
                quoteDao.save(quoteClass);
            }
            if (command.equalsIgnoreCase("find"))
            {
                quoteDao.findBySymbol(symbol);
            }
            if (command.equalsIgnoreCase("deleteall"))
            {
                quoteDao.deleteAll();
            }

        }
        catch (SQLException sqlException)
        {
            StockQuoteController.logger.error("SQLException detected!");
        }
    }
}