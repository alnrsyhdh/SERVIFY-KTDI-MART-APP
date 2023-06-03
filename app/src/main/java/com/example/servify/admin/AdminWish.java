package com.example.servify.admin;

import com.example.servify.customer.Wish;

public class AdminWish extends Wish {

    private int wishId;
    private String wishName;
    private String wishDetails;
    private String wishPic;

    public AdminWish() {
        // Default constructor required for Firebase
    }

    public AdminWish(int wishId, String wishName, String wishDetails) {
        this.wishId = wishId;
        this.wishName = wishName;
        this.wishDetails = wishDetails;
    }

    public AdminWish(int wishId, String wishName, String wishDetails, String wishPic) {
        this.wishId = wishId;
        this.wishName = wishName;
        this.wishDetails = wishDetails;
        this.wishPic = wishPic;
    }

    public int getWishId() {
        return wishId;
    }

    public void setWishId(int wishId) {
        this.wishId = wishId;
    }

    public String getWishName() {
        return wishName;
    }

    public void setWishName(String wishName) {
        this.wishName = wishName;
    }

    public String getWishDetails() {
        return wishDetails;
    }

    public void setWishDetails(String wishDetails) {
        this.wishDetails = wishDetails;
    }

    public String getWishPic() {
        return wishPic;
    }

    public void setWishPic(String wishPic) {
        this.wishPic = wishPic;
    }
}
