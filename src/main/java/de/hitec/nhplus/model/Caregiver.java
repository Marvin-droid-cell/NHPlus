package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The Caregiver class represents a caregiver and extends the Person class.
 * It includes attributes such as a unique caregiver ID and a telephone number.
 */
public class Caregiver extends Person{
    private SimpleStringProperty telNumber;
    private SimpleLongProperty cgID;

    /**
     * Constructor to create a {@code Caregiver} object with the specified parameters.
     *
     * @param cgID The unique identifier for the caregiver.
     * @param firstName The first name of the caregiver.
     * @param surname The last name of the caregiver.
     * @param telNumber The telephone number of the caregiver.
     */
    public Caregiver(long cgID,String firstName, String surname, String telNumber) {
        super(firstName, surname);
        this.cgID = new SimpleLongProperty(cgID);
        this.telNumber = new SimpleStringProperty(telNumber);
    }

    /**
     * Creates a new Caregiver instance with the given details.
     *
     * @param firstName The first name of the caregiver.
     * @param surname The last name of the caregiver.
     * @param telNumber The telephone number of the caregiver.
     */
    public Caregiver(String firstName, String surname, String telNumber) {
        super(firstName, surname);
        this.telNumber = new SimpleStringProperty(telNumber);
    }

    public long getCgID() {
        return cgID.get();
    }

    public SimpleLongProperty cgIDProperty() {
        return cgID;
    }

    public void setCgID(long cgID) {
        this.cgID.set(cgID);
    }

    public String getTelNumber() {
        return telNumber.get();
    }

    public SimpleStringProperty telNumberProperty() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber.set(telNumber);
    }

    /**
     * Returns a string representation of the Caregiver object. The representation includes
     * the caregiver ID, first name, surname, and telephone number, formatted as key-value pairs.
     *
     * @return A string describing the Caregiver object with its attributes.
     */
    @Override
    public String toString() {
        return "Pfleger" + "\nMNID: " + this.cgID +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.telNumber +
                "\n";
    }
}