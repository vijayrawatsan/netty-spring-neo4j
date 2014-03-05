package com.sitename.util;

import java.util.Date;

import com.sitename.domain.User;

public class UserDto {

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private String password;

    private String gender;
    
    private String signature;
    
    private Date signatureExpiry;
    
    public UserDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.gender = user.getGender();
        this.signature = user.getSignature();
        this.signatureExpiry = user.getSignatureExipry();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getSignatureExpiry() {
        return signatureExpiry;
    }

    public void setSignatureExpiry(Date signatureExpiry) {
        this.signatureExpiry = signatureExpiry;
    }

}
