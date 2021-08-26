package com.example.crimeandmissingpersonreporting;

import com.google.firebase.database.Exclude;

public class Person {
    private String name;
    private String imageURL;
    private String age;
    //private String key;
    private String gender;
    private String description;
    private String residence;
    private String contact;
    //private int position;

    public Person() {
        //empty constructor
    }

    public Person(String name,String imageURL,String age,String gender,String description,String residence,String contact ) {
        this.name = name;
        this.imageURL=imageURL;
        this.age = age;
        this.gender=gender;
        this.description=description;
        this.residence=residence;
        this.contact=contact;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

}
