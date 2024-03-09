package com.example.myapplication;

public class Tasks {
    private String taskId, name, description, deadline, type, status, deadlineOutput;
    private int exp;
    boolean deletable;

    public Tasks() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Tasks( String name, String description, String deadline, String type, String status, String taskId, int exp, Boolean deletable) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.type = type;
        this.status = status;
        this.taskId = taskId;
        this.exp = exp;
        this.deletable = deletable;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
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

    public String getDeadline() { return deadline; }

    public boolean getDeletable() { return deletable; }

    public void setDeletable(boolean deletable) { this.deletable = deletable; }
    public String getFormattedDeadline() {
        String[] formattedDeadline = deadline.split("/",-1);
        if (formattedDeadline.length == 3) {
            deadlineOutput = calendarMonth(Integer.valueOf(formattedDeadline[0])) + ", " + formattedDeadline[1] + " " + formattedDeadline[2];
        }
        return deadlineOutput;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    private String calendarMonth(int month){
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Null";
        }
    }
}