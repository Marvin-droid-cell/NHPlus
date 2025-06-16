package de.hitec.nhplus.datastorage;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }

    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    public PatientDao createPatientDao() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates and returns an instance of {@code CaregiverDao} to manage caregiver-related database operations.
     * This method initializes the DAO with a database connection retrieved from {@code ConnectionBuilder}.
     *
     * @return An instance of {@code CaregiverDao} configured with a valid database connection.
     */
    public CaregiverDao createCaregiverDao() {
        return new CaregiverDao(ConnectionBuilder.getConnection());
    }

    /**
     * Creates an instance of the UserDao class to manage data access operations
     * related to the User entity. This method initializes the UserDao with a database
     * connection provided by the ConnectionBuilder.
     *
     * @return an instance of UserDao configured with a database connection.
     */
    public UserDao createUserDao() {
        return new UserDao(ConnectionBuilder.getConnection());
    }
}
