package com.example.mynotes_berdnikova;

public class Notes {
    private int number;
    private String description;

    public Notes(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }
    public String getDescription() {
        return description;
    }

}