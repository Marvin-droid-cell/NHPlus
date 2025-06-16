package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The Caregiver class represents a caregiver in the system.
 * It extends the Person class and includes an additional attribute
 * for the caregiver's telephone number.
 */
public class Caregiver extends Person{
    private SimpleStringProperty telNumber;

    /**
     * Constructs a new Caregiver with the specified caregiver ID, first name, surname, and telephone number.
     *
     * @param cgID The unique identifier for the caregiver.
     * @param firstName The first name of the caregiver.
     * @param surname The surname of the caregiver.
     * @param telNumber The telephone number of the caregiver.
     */
    public Caregiver(long cgID,String firstName, String surname, String telNumber) {
        super(firstName, surname, cgID);
        this.telNumber = new SimpleStringProperty(telNumber);
    }

    /**
     * Constructs a new Caregiver with the specified first name, surname, and telephone number.
     *
     * @param firstName The first name of the caregiver.
     * @param surname The surname of the caregiver.
     * @param telNumber The telephone number of the caregiver.
     */
    public Caregiver(String firstName, String surname, String telNumber) {
        super(firstName, surname);
        this.telNumber = new SimpleStringProperty(telNumber);
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
     * Returns a string representation of the Caregiver object.
     * The string includes the caregiver's ID, first name, surname, and telephone number.
     *
     * @return A string representation of the Caregiver object.
     */
    @Override
    public String toString() {
        return "Pfleger" + "\nMNID: " + this.getId() +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.telNumber +
                "\n";
    }
}