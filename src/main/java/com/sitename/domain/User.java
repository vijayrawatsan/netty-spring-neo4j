package com.sitename.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity {
    //TODO-Index data for sure
    private String firstName;

    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String userName;
    
    @NotNull
    private String password;

    @NotNull
    private String gender;
    
    //TODO-When scaling use a separate Redis Cluster for this functionality
    private String signature;
    
    private Date signatureExipry;
    
    private Date facebookAccessTokenExpiry;
    
    private String facebookAccessToken;
    
    private String facebookId;

    public User() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

    public Date getSignatureExipry() {
        return signatureExipry;
    }

    public void setSignatureExipry(Date signatureExipry) {
        this.signatureExipry = signatureExipry;
    }

    public Date getFacebookAccessTokenExpiry() {
        return facebookAccessTokenExpiry;
    }

    public void setFacebookAccessTokenExpiry(Date facebookAccessTokenExpiry) {
        this.facebookAccessTokenExpiry = facebookAccessTokenExpiry;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
