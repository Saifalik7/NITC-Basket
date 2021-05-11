package com.example.nitcbasket.Model;

public class OrderItem {

    private String Date, Oid, Time, address, contact, name,status,userId,totalAmount,quantity;

    public OrderItem()
    {

    }
    public OrderItem(String quantity,String date,String oid, String time, String address, String contact, String name, String status,String userId,String totalAmount) {
        Date = date;
        Oid = oid;
        Time = time;
        this.address = address;
        this.contact = contact;
        this.name = name;
        this.status = status;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.quantity= quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOid() {
        return Oid;
    }

    public void setOid(String oid) {
        Oid = oid;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
