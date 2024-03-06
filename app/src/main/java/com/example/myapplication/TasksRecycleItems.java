package com.example.myapplication;

public class TasksRecycleItems {

    String taskName;
    String taskDescription;
    String taskDeadline;
    String taskStatus;
    String taskId;
    float exp;

    public TasksRecycleItems(String taskName, String taskDescription, String taskDeadline, String taskStatus, String taskId, float exp) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.taskStatus = taskStatus;
        this.taskId = taskId;
        this.exp = exp;
    }

    public float getExp() {
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
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTaskDescription() {
        return taskDescription;
    }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
    public String getTaskDeadline() {
        return taskDeadline;
    }
    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

}
