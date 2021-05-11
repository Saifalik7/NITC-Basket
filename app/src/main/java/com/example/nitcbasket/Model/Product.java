package com.example.nitcbasket.Model;

public class Product {
    private String Time, Date, Image, Pid, Pname, Price, Quantity;

    public Product()
    {

    }

    public Product(String time, String date, String image, String pid, String pname, String price, String quantity) {
        Time = time;
        Date = date;
        Image = image;
        Pid = pid;
        Pname = pname;
        Price = price;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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
}
