package com.fortuneteller.cup.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Answer {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String message;
    //@ColumnInfo(name = "If i want to change the name of the column")
    private int resource;
    private long created;

    public Answer(String message, int resource) {
        this.message = message;
        this.resource = resource;
        this.created = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
