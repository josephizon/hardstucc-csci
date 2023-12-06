package com.example.myapplication;

public class Tasks {
    private String taskId, name, description, deadline, type,status;

    public Tasks() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Tasks( String name, String description, String deadline, String type, String status, String taskId) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.type = type;
        this.status = status;
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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