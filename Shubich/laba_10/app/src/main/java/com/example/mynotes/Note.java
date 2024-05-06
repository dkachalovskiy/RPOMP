package com.example.mynotes;
public class Note {
    private int id;
    private String description;

    public Note(String description) {
        this.description = description;
    }

    public Note() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
