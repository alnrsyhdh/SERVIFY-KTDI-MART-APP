package com.example.servify;

public class FormData {
    private int id;
    private String pname;
    private String uname;

    private String wish;
    private String imageUrl;

    public FormData() {
        // Default constructor required for Firebase
    }

    public FormData(int id, String uname, String pname, String wish, String imageUrl) {
        this.id = id;
        this.uname = uname;
        this.pname = pname;
        this.wish = wish;
        this.imageUrl = imageUrl;
    }

    public int getid() {return id; }

    public String getuName() {
        return uname;
    }

    public String getpName() {
        return pname;
    }

    public String getWish(){return wish;}

    public String getImageUrl() {
        return imageUrl;
    }
}
