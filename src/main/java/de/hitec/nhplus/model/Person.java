package de.hitec.nhplus.model;

import javafx.beans.property.SimpleStringProperty;

public abstract class Person {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty surname;

    /**
     * Constructs a new Person with the specified first name and surname.
     *
     * @param firstName The first name of the person.
     * @param surname The surname of the person.
     */
    public Person(String firstName, String surname) {
        this.firstName = new SimpleStringProperty(firstName);
        this.surname = new SimpleStringProperty(surname);
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
