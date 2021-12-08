package com.example.androidaccidentapp;

public class Report {
    private String accidentLocation;
    private String name;

    public Report(){

    }
    public Report(String accidentLocation, String name) {
        this.accidentLocation = accidentLocation;
        this.name = name;
    }

    public String getAccidentLocation() {
        return accidentLocation;
    }

    public void setAccidentLocation(String accidentLocation) {
        this.accidentLocation = accidentLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
