package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents an abstract concept of a person which includes attributes
 * for first name, surname, and optionally an ID. This class provides
 * methods for accessing and modifying these attributes.
 */
public abstract class Person {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty surname;
    private SimpleLongProperty id = null;

    /**
     * Constructs a Person object with the specified first name, surname, and ID.
     *
     * @param firstName The first name of the person.
     * @param surname The surname of the person.
     * @param id The unique identifier for the person.
     */
    public Person(String firstName, String surname, long id) {
        this.firstName = new SimpleStringProperty(firstName);
        this.surname = new SimpleStringProperty(surname);
        this.id = new SimpleLongProperty(id);
    }

    /**
     * Constructs a new Person with the specified first name and surname.
     *
     * @param firstName The first name of the person.
     * @param surname The surname of the person.
     */
    public Person(String firstName, String surname) {
        this.firstName =  new SimpleStringProperty(firstName);
        this.surname = new SimpleStringProperty(surname);
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public long getId() {
        return id.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }
}
