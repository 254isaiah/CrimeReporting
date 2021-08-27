package com.example.crimeandmissingpersonreporting;

public class ComplainModel {
    private String imageUrl;
    private String names;
    private String age;
    private String gender;
    private String complain;
    private String residence;
    private String contact;
    private String iDNo;
    private String date;

    public ComplainModel(){

    }

    public ComplainModel(String imageUrl, String names, String age, String gender, String complain, String residence, String contact, String iDNo, String date) {
        if (imageUrl.trim().equals("")) {
            imageUrl = "No Image";
        }

        this.imageUrl = imageUrl;
        this.names = names;
        this.age = age;
        this.gender = gender;
        this.complain = complain;
        this.residence = residence;
        this.contact = contact;
        this.iDNo = iDNo;
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getiDNo() {
        return iDNo;
    }

    public void setiDNo(String iDNo) {
        this.iDNo = iDNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
