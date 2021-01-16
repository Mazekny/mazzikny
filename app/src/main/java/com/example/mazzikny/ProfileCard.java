package com.example.mazzikny;

import java.io.Serializable;

public class ProfileCard implements Serializable {
    private int userImage;
    private String userName;
    private String userInstrument;
    private String userProf;
    private String fbLink;
    private String twLink;
    private String email;
    private String phoneNumber;
    private String address;
    private String exp;

    public ProfileCard(int userImage, String userName, String userInstrument, String userProf, String fbLink, String twLink, String email, String phoneNumber, String address, String exp) {
        this.userImage = userImage;
        this.userName = userName;
        this.userInstrument = userInstrument;
        this.userProf = userProf;
        this.fbLink = fbLink;
        this.twLink = twLink;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.exp = exp;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserInstrument() {
        return userInstrument;
    }

    public String getUserProf() {
        return userProf;
    }

    public String getFbLink() {
        return fbLink;
    }

    public String getTwLink() {
        return twLink;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getExp() {
        return exp;
    }
}
