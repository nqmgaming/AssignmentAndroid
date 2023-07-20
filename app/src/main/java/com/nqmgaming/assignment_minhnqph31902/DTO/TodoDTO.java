package com.nqmgaming.assignment_minhnqph31902.DTO;

public class TodoDTO {
    private int id;
    private String name;
    private String content;
    private int status;
    private String startDate;
    private String endDate;
    private int userId;

    public TodoDTO() {
    }

    public TodoDTO(int id, String name, String content, int status, String startDate, String endDate, int userId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
