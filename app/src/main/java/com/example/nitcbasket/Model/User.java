package com.example.nitcbasket.Model;

public class User {

    String name, email, contact, password,Uid, currStatus, refuseCount;
    String image;
    public User(){

   }
    public User(String name, String email, String contact, String password,String Uid,String image,String currStatus,String refuseCount) {

        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
        this.Uid = Uid;
        this.image = image;
        this.currStatus = currStatus;
        this.refuseCount = refuseCount;
    }

    public String getRefuseCount() {
        return refuseCount;
    }

    public void setRefuseCount(String refuseCount) {
        this.refuseCount = refuseCount;
    }

    public String getCurrStatus() {
        return currStatus;
    }

    public void setCurrStatus(String currStatus) {
        this.currStatus = currStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
