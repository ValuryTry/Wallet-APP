package com.ensam.demo;
import javafx.beans.property.*;
import java.math.BigDecimal;

public class User {
    private int databaseId; // The actual ID from the database

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    private IntegerProperty id;
    private StringProperty firstName;
    private StringProperty lastName;
    private IntegerProperty age;
    private ObjectProperty<BigDecimal> soldAccount;
    private StringProperty username;
    private StringProperty password;
    private StringProperty gender;
    private StringProperty occupation;

    // Constructeur
    public User(int id, String firstName, String lastName, int age, BigDecimal soldAccount, String username, String password, String gender, String occupation) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.age = new SimpleIntegerProperty(age);
        this.soldAccount = new SimpleObjectProperty<>(soldAccount);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.gender = new SimpleStringProperty(gender);
        this.occupation = new SimpleStringProperty(occupation);
    }

    // Getters et setters avec les propriétés JavaFX
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public ObjectProperty<BigDecimal> soldAccountProperty() {
        return soldAccount;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty occupationProperty() {
        return occupation;
    }

    // Getters classiques pour récupérer les valeurs de chaque propriété
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public BigDecimal getSoldAccount() {
        return soldAccount.get();
    }

    public void setSoldAccount(BigDecimal soldAccount) {
        this.soldAccount.set(soldAccount);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getOccupation() {
        return occupation.get();
    }

    public void setOccupation(String occupation) {
        this.occupation.set(occupation);
    }
}
