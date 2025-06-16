package de.hitec.nhplus.model;

/**
 * Represents a user with a username and password.
 * This class provides methods to get and set the username and password
 * and override the toString method to return a string representation of a user object.
 */
public class User {
    private String name;
    private String password;

    /**
     * Constructor to initiate an object of class <code>Patient</code> with the given parameter.
     *
     * @param name Username
     * @param password user password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the User object.
     * The string includes the username and password of the user.
     *
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        return "User" +
                "\nUsername: " + this.getName() +
                "\nPassword: " + this.getPassword() +
                "\n";
    }
}
