package com.example.nitcbasket.Model;

public class Feedback {
    String Feedback, Name, Oid;

    public Feedback()
    {

    }

    public Feedback(String feedback, String name, String oid) {
        Feedback = feedback;
        Name = name;
        Oid = oid;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOid() {
        return Oid;
    }

    public void setOid(String oid) {
        Oid = oid;
    }
}
