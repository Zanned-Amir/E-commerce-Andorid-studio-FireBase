package com.sph.sbh.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User {

    private String name;
    private String lastName;
    private String birthDate;
    private String gender;
    private String email;
    private String phoneNumber;
    private String state;
    private String addresse1;
    private String addresse2;



    private String registrationDate; // Change type to String

    public void setRegistrationDateToCurrent() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(calendar.getTimeZone());
        this.registrationDate = sdf.format(calendar.getTime());
    }

    public String getAddresse1() {
        return addresse1;
    }

    public void setAddresse1(String addresse1) {
        this.addresse1 = addresse1;
    }

    public String getAddresse2() {
        return addresse2;
    }

    public void setAddresse2(String addresse2) {
        this.addresse2 = addresse2;
    }

    public User(String email, String name, String lastName, String phoneNumber, String state, String birthDate, String gender, String addresse1, String addresse2) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.birthDate = birthDate;
        this.gender = gender;
        this.addresse1 = addresse1;
        this.addresse2 = addresse2;
        setRegistrationDateToCurrent();
    }

    // Getters and setters for all fields
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}

