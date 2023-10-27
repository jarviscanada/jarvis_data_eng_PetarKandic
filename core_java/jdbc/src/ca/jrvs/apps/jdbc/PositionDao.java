package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Just like quoteDAO, this provides an abstract layer between the service class and the database.
 */
public class PositionDao implements CrudDao<Position, Integer>
{
    private Connection connection;

    /**
     * Constructor method, accepts a connection.
     * @param connection
     */
    public PositionDao(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Saves a position (bought stock) to the database.
     * @param entity - must not be null
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Position save(Position entity) throws IllegalArgumentException
    {
        if (entity == null)
        {
            StockQuoteController.logger.error("Entry already exists!");
        }

        Optional<Position> existingPosition = findById(entity.getId());
        if (existingPosition.isPresent())
        {
            try
            {
                updatePosition(entity);
            }
            catch (SQLException sqlException)
            {
                StockQuoteController.logger.error("SQLException detected!");
            }
        }
        else
        {
            try
            {
                insertPosition(entity);
            }
            catch (SQLException sqlException)
            {
                StockQuoteController.logger.error("SQLException detected!");
            }
        }
        return entity;
    }

    /**
     * Finds a given position by symbol.
     * @param symbol
     * @return the position with associated symbol.
     * @throws IllegalArgumentException
     */
    public List<Position> findBySymbol(String symbol) throws IllegalArgumentException
    {
        if (symbol == null || symbol.trim().isEmpty())
        {
            StockQuoteController.logger.error("ID must not be null");
        }
        List<Position> quotes = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quote WHERE symbol = ?");
            preparedStatement.setString(1, symbol);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                quotes.add(mapResultSetToPosition(resultSet));
            }
        }
        catch (SQLException sqlException)
        {
            StockQuoteController.logger.error("There was an issue with finding the ID!");

        }
        return quotes;
    }

    /**
     * Finds a given position by ID.
     * @param id - must not be null
     * @return the position, if it exists.
     * @throws IllegalArgumentException
     */
    public Optional<Position> findById(Integer id) throws IllegalArgumentException
    {
        if (id == null)
        {
            StockQuoteController.logger.error("ID must not be null");
        }
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM position WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                return Optional.of(mapResultSetToPosition(resultSet));
            }
        }
        catch (SQLException sqlException)
        {
            StockQuoteController.logger.error("SQLException detected!");
        }

        return Optional.empty();
    }

    /**
     * Finds all positions.
     * @return the list of positions.
     */
    @Override
    public Iterable<Position> findAll()
    {
        List<Position> positions = new ArrayList<>();

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM position");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                positions.add(mapResultSetToPosition(resultSet));
            }
        }
        catch (SQLException sqlException)
        {
            StockQuoteController.logger.error("SQLException detected!");
        }

        return positions;
    }

    /**
     * Deletes the position with the given ID.
     * @param id - must not be null
     * @throws IllegalArgumentException
     */
    public void deleteById(Integer id) throws IllegalArgumentException
    {
        if (id == null)
        {
            StockQuoteController.logger.error("ID must not be null");
        }
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM position WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            StockQuoteController.logger.error("SQLException detected!");
        }
    }

    @Override
    public void deleteAll()
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM position");
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            StockQuoteController.logger.error("SQLException detected!");
        }
    }

    /**
     * Adds a given position to the database.
     * @param position
     * @throws SQLException
     */
    private void insertPosition(Position position) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO position (symbol, num_of_shares, value_paid) VALUES (?, ?, ?)");

        preparedStatement.setString(1, position.getSymbol());
        preparedStatement.setInt(2, position.getNumOfShares());
        preparedStatement.setDouble(3, position.getValuePaid());
        preparedStatement.executeUpdate();
    }

    /**
     * Updates a given position.
     * @param position
     * @return the updated position
     * @throws SQLException
     */
    private Optional<Position> updatePosition(Position position) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE position SET symbol = ?, num_of_shares = ?, value_paid = ? WHERE id = ?");

        preparedStatement.setString(1, position.getSymbol());
        preparedStatement.setInt(2, position.getNumOfShares());
        preparedStatement.setDouble(3, position.getValuePaid());
        preparedStatement.setInt(4, position.getId());

        preparedStatement.executeUpdate();
        return this.findById(position.getId());
    }

    /**
     * Maps the results to a Position.
     * @param resultSet
     * @return the Position, with added data.
     * @throws SQLException
     */
    private Position mapResultSetToPosition(ResultSet resultSet) throws SQLException {
        Position position = new Position();
        position.setId(resultSet.getInt("id"));
        position.setSymbol(resultSet.getString("symbol"));
        position.setNumOfShares(resultSet.getInt("num_of_shares"));
        position.setValuePaid(resultSet.getDouble("value_paid"));
        return position;
    }
}
