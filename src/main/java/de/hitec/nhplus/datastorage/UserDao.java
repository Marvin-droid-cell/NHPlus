package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * UserDao is a concrete implementation of the generic DaoImp class designed to manage User entities.
 * It provides functionality to interact with database tables associated with User objects
 * using various CRUD operations.
 *
 * This class defines how User objects are mapped to and from database entries.
 * Each method is tailored to handle operations specific to the User entity.
 */
public class UserDao extends DaoImp<User>{
    /**
     * Constructs a new instance of the UserDao class. The class is responsible
     * for data access operations related to the User entity. It uses the provided
     * database connection for executing SQL operations.
     *
     * @param connection The database connection to be used for all SQL operations.
     */
    public UserDao(Connection connection) {
        super(connection);
    }

    /**
     * Retrieves an instance of the User object from the provided ResultSet.
     * This method extracts user data from the ResultSet and converts it into
     * a User object with the relevant fields populated.
     *
     * @param result The ResultSet containing the user data retrieved from the database.
     * @return A User object with the data mapped from the given ResultSet.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    @Override
    protected User getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new User(
                result.getString(1),
                result.getString(2));
    }

    /**
     * Retrieves a list of User objects from the provided ResultSet by iterating through
     * all rows and mapping each row to a User instance.
     *
     * @param result The ResultSet containing data retrieved from the database. Each row
     *               in the ResultSet corresponds to a User record.
     * @return An ArrayList of User objects populated with data from the ResultSet.
     * @throws SQLException if an SQL exception occurs while accessing the ResultSet.
     */
    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        while (result.next()) {
            User user = new User(
                    result.getString(1),
                    result.getString(2));
            list.add(user);
        }
        return list;    }

    /**
     * Creates a PreparedStatement for inserting a User object into the database.
     * This method prepares an SQL INSERT statement with the username and password provided by the User object.
     *
     * @param user The User object containing the username and password to be inserted into the database.
     * @return A PreparedStatement object configured to execute the SQL INSERT statement
     *         for the provided User object. Returns null if a database error occurs while creating the statement.
     */
    @Override
    protected PreparedStatement getCreateStatement(User user) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO user (username, password)" +
                    "VALUES (?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Prepares and returns a SQL statement to retrieve all entries from the "user" table.
     * This method constructs a {@link PreparedStatement} object using the database connection
     * and a predefined query. If an SQL error occurs during statement preparation, the
     * exception stack trace is printed, and the method returns null.
     *
     * @return a {@link PreparedStatement} for executing a query to retrieve all records
     *         from the "user" table, or null if there is a SQL error.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM user";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;    }

    @Override
    protected PreparedStatement getReadByIDStatement(long key) {
        return null;
    }

    @Override
    protected PreparedStatement getUpdateStatement(User user) {
        return null;
    }

    @Override
    protected PreparedStatement getDeleteStatement(long key) {
        return null;
    }


}
