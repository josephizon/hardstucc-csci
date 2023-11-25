package com.example.myapplication;

public class Users {
    private String email, firstName, middleName, lastName, buddyUid;
    private Users buddy;  // New field for the buddy

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String email, String firstName, String middleName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBuddyUid() {
        return buddyUid;
    }

    public void setBuddyUid(String buddyUid) {
        this.buddyUid = buddyUid;
    }
}