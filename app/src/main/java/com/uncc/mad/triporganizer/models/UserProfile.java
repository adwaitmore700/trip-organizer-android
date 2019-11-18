package com.uncc.mad.triporganizer.models;


//This model consists of the User Auth data along with user profile data

public class UserProfile {
    //Optional for other users, Mandatory for logged in user
    private String Id;  //App generated id
    private String UserId;  //Firebase generated id
    private String Username;
    private String EmailAddress;    //optional since username and email id would be the same
    private String Password;
    private String AuthToken;

    private String FirstName;
    private String LastName;
    private int GenderId;       //optional
    private String Gender;
    private String ProfilePhotoUri;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProfilePhoto() {
        return ProfilePhotoUri;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhotoUri = profilePhoto;
    }
}
