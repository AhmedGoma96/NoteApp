package com.example.noteflow;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    String name;
    String description;
    int best;
    String date;
    String time;

    public Note(String name, String description, int best,String time,String date) {
        this.name = name;
        this.description = description;
        this.best = best;
        this.time=time;
        this.date=date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
