package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Patient class represents a patient in a medical context, extending the Person
 * class. It contains information such as the patient's date of birth, care level, and
 * room number. Additionally, it can store a list of the patient's treatments.
 */
public class Patient extends Person {
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty careLevel;
    private final SimpleStringProperty roomNumber;
    private final List<Treatment> allTreatments = new ArrayList<>();

    /**
     * Constructs a new Patient with the specified first name, surname, date of birth, care level, and room number.
     *
     * @param firstName The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The date of birth of the patient, represented as a LocalDate.
     * @param careLevel The level of care required by the patient.
     * @param roomNumber The room number assigned to the patient.
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber) {
        super(firstName, surname);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
    }

    /**
     * Constructs a new Patient with the specified parameters.
     *
     * @param pid The unique identifier for the patient.
     * @param firstName The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The date of birth of the patient as a LocalDate.
     * @param careLevel The care level of the patient.
     * @param roomNumber The room number assigned to the patient.
     */
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber) {
        super(firstName, surname, pid);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
    }


    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    /**
     * Stores the given string as new <code>birthOfDate</code>.
     *
     * @param dateOfBirth as string in the following format: YYYY-MM-DD.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getCareLevel() {
        return careLevel.get();
    }

    public SimpleStringProperty careLevelProperty() {
        return careLevel;
    }

    public void setCareLevel(String careLevel) {
        this.careLevel.set(careLevel);
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }


    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    /**
     * Adds a treatment to the list of treatments for the patient if it is not already present.
     *
     * @param treatment The treatment to be added to the patient's list of treatments.
     * @return true if the treatment was successfully added, false if it was already present.
     */
    public boolean add(Treatment treatment) {
        if (this.allTreatments.contains(treatment)) {
            return false;
        }
        this.allTreatments.add(treatment);
        return true;
    }

    /**
     * Returns a string representation of the Patient object.
     * The string includes the patient's ID, first name, surname, date of birth,
     * care level, and room number.
     *
     * @return A string representation of the Patient object.
     */
    @Override
    public String toString() {
        return "Patient" + "\nMNID: " + this.getId() +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.dateOfBirth +
                "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomNumber +
                "\n";
    }
}