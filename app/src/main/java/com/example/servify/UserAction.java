package com.example.servify;

public class UserAction {
    private String user;
    private String action;

    public UserAction() {
        // Required empty constructor for Firebase serialization
    }

    public UserAction(String user, String action) {
        this.user = user;
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
