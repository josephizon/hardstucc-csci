package com.example.myapplication;

public class Tasks {
    private String taskId, name, description, deadline;

    public Tasks() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Tasks( String name, String description, String deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}