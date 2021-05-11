package com.example.nitcbasket.Model;

public class CartItem {

    private String Pid, Pname,Price,Quantity,Time,Date;

    public CartItem(){

    }

    public CartItem(String pid, String pname, String price, String quantity, String time, String date) {

        Pid = pid;
        Pname = pname;
        Price = price;
        Quantity = quantity;
        Time = time;
        Date = date;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
