package com.example.servify.admin;

import android.widget.EditText;

public class Product {

        private int productId;
        public String productName;

        public double productPrice;
        public String productPic;

        public Product(int productId, EditText productName, EditText productPrice) {
            // Default constructor required for Firebase
        }

        public Product(int productId, String productName, double productPrice) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
        }

    public Product(int productId, String productName, double productPrice, String productPic) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPic = productPic;
    }

        //getter and setter for ProductId
        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }


        //getter and setter for ProductName
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        //getter and setter for ProductPrice
        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }
}

