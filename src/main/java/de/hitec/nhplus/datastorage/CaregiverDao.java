package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Caregiver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The CaregiverDao class provides an implementation for managing caregiver records in a database.
 * This class extends the generic {@code DaoImp} class, utilizing {@code Caregiver} as the type parameter.
 * It defines specific SQL statements and operations for creating, reading, updating, and deleting caregiver data.
 *
 * The methods in this class work in conjunction with the {@code DaoImp} and {@code Dao} abstract classes and interfaces.
 * It uses {@code PreparedStatement} objects to interact with the database and to ensure SQL query safety.
 *
 * Key functionalities of this class include:
 * - Translating {@code ResultSet} data into {@code Caregiver} objects or lists of {@code Caregiver}.
 * - Executing SQL operations for inserting, reading, updating, and deleting caregiver records.
 */
public class CaregiverDao extends DaoImp<Caregiver> {

    /**
     * Constructs a new instance of the CaregiverDao class. This class is responsible
     * for handling data access operations related to the Caregiver entity using the provided
     * database connection for executing SQL operations.
     *
     * @param connection The database connection to be used for all SQL operations.
     */
    public CaregiverDao(Connection connection) {
        super(connection);
    }

    /**
     * Maps a single row from a {@code ResultSet} to an instance of the {@code Caregiver} class.
     * The {@code ResultSet} is expected to contain the following columns in order:
     * - Column 1: {@code cgID} (Integer)
     * - Column 2: {@code firstName} (String)
     * - Column 3: {@code surname} (String)
     * - Column 4: {@code telNumber} (String)
     *
     * @param set The {@code ResultSet} containing the data to be mapped to a {@code Caregiver} object.
     * @return A new instance of {@code Caregiver} populated with data from the {@code ResultSet}.
     * @throws SQLException If an SQL error occurs while reading data from the {@code ResultSet}.
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet set) throws SQLException {
        return new Caregiver(
                set.getInt(1),
                set.getString(2),
                set.getString(3),
                set.getString(4)
        );
    }

    /**
     * Extracts a list of Caregiver objects from a given ResultSet.
     * Each row in the ResultSet is mapped to a Caregiver object.
     *
     * @param set the ResultSet containing caregiver data to be transformed into objects.
     *            It should represent rows of caregivers in the database.
     * @return an ArrayList of Caregiver objects populated with data from the ResultSet.
     * @throws SQLException if any SQL error occurs while accessing the ResultSet.
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        while (set.next()) {
            list.add(new Caregiver(
                    set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4)));
        }
        return list;
    }

    /**
     * Generates a PreparedStatement to insert a new Caregiver record into the database.
     * The statement will insert the firstname, surname, and telephone number of the given Caregiver.
     *
     * @param caregiver The Caregiver object containing data to be inserted into the database.
     *                  Must have valid firstname, surname, and telephone number values.
     * @return A PreparedStatement object ready to execute the insert operation.
     *         Returns null if a SQLException occurs during statement preparation.
     */
    @Override
    protected PreparedStatement getCreateStatement(Caregiver caregiver) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO caregiver (firstname, surname, telnumber) " +
                    "VALUES (?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getTelNumber());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a PreparedStatement to read a caregiver record from the database by its unique ID.
     *
     * @param cgID The unique identifier of the caregiver to be retrieved.
     * @return A PreparedStatement for executing the query to retrieve the caregiver by ID,
     *         or null if an SQLException occurs during statement preparation.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long cgID) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM caregiver WHERE cgID = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, cgID);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a PreparedStatement to retrieve all rows from the caregiver table.
     * The PreparedStatement is constructed using a pre-defined SQL query.
     * In case of a SQLException, the exception is caught and stack trace is printed.
     *
     * @return PreparedStatement to query all rows from the caregiver table,
     * or null if an exception occurs during statement preparation.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM caregiver";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    /**
     * Generates a PreparedStatement to update a Caregiver entity in the database.
     * The query updates the firstname, surname, and telNumber fields of the
     * caregiver record identified by cgID.
     *
     * @param caregiver The Caregiver object containing the updated data to be persisted.
     *                  This includes the Caregiver's ID, firstname, surname, and phone number.
     * @return A PreparedStatement object configured to update the specified caregiver in
     *         the database. Returns null if an SQLException occurs during the statement preparation.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Caregiver caregiver) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL =
                    "UPDATE caregiver SET " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "telNumber = ?" +
                            "WHERE cgID = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getTelNumber());
            preparedStatement.setLong(4, caregiver.getCgID());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a PreparedStatement to delete a caregiver record from the database
     * based on the provided caregiver ID.
     *
     * @param cgID The ID of the caregiver to be deleted.
     * @return A PreparedStatement object configured to execute the deletion query.
     *         Returns null if an SQLException occurs during statement preparation.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long cgID) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM caregiver WHERE cgID = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, cgID);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
}
