package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class serves as an abstract layer between the service classes and the database.
 * It provides several different methods, including the ability to save and remove stocks.
 */
public class QuoteDao implements CrudDao<Quote, Integer> {

    private Connection connection;
    final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);

    /**
     * Constructor method, accepts a connection.
     * @param connection
     */
    public QuoteDao(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Saves stocks with the associated symbol to the Quote table.
     * @param entity - must not be null
     * @return the saved quote.
     * @throws IllegalArgumentException
     */
    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (entity == null)
        {
            StockQuoteController.logger.error("Entry already exists!");
        }
        // Check if the entity already exists
        Optional<Quote> existingQuote = findById(entity.getId());
        if (existingQuote.isEmpty())
        {
            try
            {
                insertQuote(entity);
            }
            catch (SQLException newException)
            {
                StockQuoteController.logger.error("SQLException detected!");
            }
        }
        if (existingQuote.isPresent())
        {
            try
            {
                // Update the existing entity
                updateQuote(entity);
            }
            catch (SQLException sqlException)
            {
                StockQuoteController.logger.error("SQLException detected!");
            }
        }

        return entity;
    }


    /**
     * Finds a row in the table by its ID.
     * Note that ID is unique.
     * @param id - must not be null
     * @return the associated Quote, if the ID is found.
     * @throws IllegalArgumentException
     */
    @Override
    public Optional<Quote> findById(Integer id) throws IllegalArgumentException
    {
        if (id == null)
        {
            StockQuoteController.logger.error("ID must not be null");
        }
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quote WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {

                return Optional.of(mapResultSetToQuote(resultSet));
            }
        }
        catch (SQLException newException)
        {
            StockQuoteController.logger.error("There was an issue with finding the ID!");
        }

        return Optional.empty();
    }

    /**
     * Finds, and returns, all rows in the table.
     * @return all rows (if any)
     */
    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quote");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                quotes.add(mapResultSetToQuote(resultSet));
            }
        }
        catch (SQLException newException)
        {
            StockQuoteController.logger.error("There was an issue with finding the quotes!");
        }

        return quotes;
    }

    /**
     * Finds a row in Quote based on its symbol.
     * @param symbol
     * @return the associated Quote (assuming it exists).
     * @throws IllegalArgumentException
     */
    public List<Quote> findBySymbol(String symbol) throws IllegalArgumentException {
        if (symbol == null || symbol.trim().isEmpty())
        {
            StockQuoteController.logger.error("Symbol must not be null or empty");
        }
        List<Quote> quotes = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quote WHERE symbol = ?");
            preparedStatement.setString(1, symbol);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                quotes.add(mapResultSetToQuote(resultSet));
            }
        }
        catch (SQLException e)
        {
            StockQuoteController.logger.error("There was an issue with finding the symbol!");
        }
        return quotes;
    }

    /**
     * Deletes a specific row in the table.
     * @param id - must not be null
     * @throws IllegalArgumentException
     */
    @Override
    public void deleteById(Integer id) throws IllegalArgumentException {
        if (id == null)
        {
            StockQuoteController.logger.error("ID must not be null!");
        }
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM quote WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            StockQuoteController.logger.error("There was an error finding the ID!");
        }
    }

    /**
     * Deletes every row in the able.
     */
    @Override
    public void deleteAll()
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM quote");
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            StockQuoteController.logger.error("There was an issue with deletion!");
        }
    }

    /**
     * Inserts a quote into the table.
     * The classs PreparedStatement is used to prevent injections.
     * @param quote
     * @throws SQLException
     */
    private void insertQuote(Quote quote) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            //Note that the primary key (ID) is not inserted
            preparedStatement.setString(1, quote.getSymbol());
            preparedStatement.setDouble(2, quote.getOpen());
            preparedStatement.setDouble(3, quote.getHigh());
            preparedStatement.setDouble(4, quote.getLow());
            preparedStatement.setDouble(5, quote.getPrice());
            preparedStatement.setInt(6, quote.getVolume());
            preparedStatement.setDate(7, quote.getLatestTradingDay());
            preparedStatement.setDouble(8, quote.getPreviousClose());
            preparedStatement.setDouble(9, quote.getChange());
            preparedStatement.setString(10, quote.getChangePercent());
            preparedStatement.setTimestamp(11, quote.getTimestamp());
            preparedStatement.executeUpdate();
    }

    /**
     * Updated a given quote.
     * @param quote
     * @return The new quote.
     * @throws SQLException
     */
    private Optional<Quote> updateQuote(Quote quote) throws SQLException
    {
        //Prevents nullPointerExceptions
        Optional<Quote> quote1 = null;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE quote SET symbol = ?, open = ?, high = ?, low = ?, price = ?, volume = ?, " +
                        "latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE id = ?");

        preparedStatement.setString(1, quote.getSymbol());
        preparedStatement.setDouble(2, quote.getOpen());
        preparedStatement.setDouble(3, quote.getHigh());
        preparedStatement.setDouble(4, quote.getLow());
        preparedStatement.setDouble(5, quote.getPrice());
        preparedStatement.setInt(6, quote.getVolume());
        preparedStatement.setDate(7, quote.getLatestTradingDay());
        preparedStatement.setDouble(8, quote.getPreviousClose());
        preparedStatement.setDouble(9, quote.getChange());
        preparedStatement.setString(10, quote.getChangePercent());
        preparedStatement.setTimestamp(11, quote.getTimestamp());
        preparedStatement.setInt(12, quote.getId());

        preparedStatement.executeUpdate();
        quote1 = this.findById(quote.getId());
        return quote1;
    }

    /**
     * Generates a quote with data gathered in QuoteHTTPHelper.
     * @param resultSet
     * @return a Quote, filled with data.
     * @throws SQLException
     */
    private Quote mapResultSetToQuote(ResultSet resultSet) throws SQLException {
        Quote quote = new Quote();
        quote.setId(resultSet.getInt("id"));
        quote.setSymbol(resultSet.getString("symbol"));
        quote.setOpen(resultSet.getDouble("open"));
        quote.setHigh(resultSet.getDouble("high"));
        quote.setLow(resultSet.getDouble("low"));
        quote.setPrice(resultSet.getDouble("price"));
        quote.setVolume(resultSet.getInt("volume"));
        quote.setLatestTradingDay(resultSet.getDate("latest_trading_day"));
        quote.setPreviousClose(resultSet.getDouble("previous_close"));
        quote.setChange(resultSet.getDouble("change"));
        quote.setChangePercent(resultSet.getString("change_percent"));
        quote.setTimestamp(resultSet.getTimestamp("timestamp"));
        return quote;
    }
}