package com.example.servify.admin;

public class Admin {

    private String email;
    private String password;
    private String role;
    private String profilePic;

    public Admin(){

    }

    public Admin(String email, String password, String role, String profilePic){
        this.email = email;
        this.password = password;
        this.role = role;
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
