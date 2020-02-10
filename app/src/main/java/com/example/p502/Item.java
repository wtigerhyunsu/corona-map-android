package com.example.p502;

import java.util.ArrayList;

public class Item {
    String id;
    String name;
    String sex;
    int age;
    String nationality;
    int meet;
    int risk;
    String img;
    ArrayList<corona_location> loc;

    public Item() {
    }

    public Item(String id, String name, String sex, int age, String nationality, int meet, int risk, String img, ArrayList<corona_location> loc) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.nationality = nationality;
        this.meet = meet;
        this.risk = risk;
        this.img = img;
        this.loc = loc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getMeet() {
        return meet;
    }

    public void setMeet(int meet) {
        this.meet = meet;
    }

    public int getRisk() {
        return risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<corona_location> getLoc() {
        return loc;
    }

    public void setLoc(ArrayList<corona_location> loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", nationality='" + nationality + '\'' +
                ", meet=" + meet +
                ", risk=" + risk +
                ", img='" + img + '\'' +
                ", loc=" + loc +
                '}';
    }
}
