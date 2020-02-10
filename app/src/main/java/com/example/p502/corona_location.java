package com.example.p502;

import java.io.Serializable;

public class corona_location implements Serializable {


    int no;
    String name;
    double lat;
    double lon;

    public corona_location() {
    }

    public corona_location(int no, String name, double lat, double lon) {
        this.no = no;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "corona_location{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
