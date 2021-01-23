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
    private String likes;
    private String dislikes;
    private String id;
    private int sentFlag;

    public ProfileCard(String id, int userImage, String userName, String userInstrument, String userProf, String fbLink, String twLink, String email, String phoneNumber, String address, String exp, String likes, String dislikes) {
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
        this.likes = likes;
        this.dislikes = dislikes;
        this.id = id;
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

    public String getLikes() {
        return likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public String getId() {
        return id;
    }

    public void setSentFlag(int flag)
    {
        this.sentFlag = flag;
    }

    public int getSentFlag()
    {
        return sentFlag;
    }
}
