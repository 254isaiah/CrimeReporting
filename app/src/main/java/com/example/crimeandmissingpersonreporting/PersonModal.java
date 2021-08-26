package com.example.crimeandmissingpersonreporting;

public class PersonModal {
    private String imageUrl;
    private String names;
    private String age;
    private String gender;
    private String description;
    private String residence;
    private String contact;
    private String birthCertNo;
    private String date;

    public PersonModal(){

    }
    public PersonModal(String imageUrl, String names, String age, String gender, String description, String residence, String contact, String birthCertNo, String date){
        this.imageUrl = imageUrl;
        this.names = names;
        this.age = age;
        this.gender = gender;
        this.description = description;
        this.residence = residence;
        this.contact = contact;
        this.birthCertNo = birthCertNo;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getBirthCertNo() {
        return birthCertNo;
    }

    public void setBirthCertNo(String birthCertNo) {
        this.birthCertNo = birthCertNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
