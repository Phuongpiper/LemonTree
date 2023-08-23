package com.example.entity;

public class EmailData {
    private Product product; // Đối tượng Product chứa thông tin sản phẩm
    private String recipientEmail; // Địa chỉ email người nhận

    // Constructors, getters, setters

    public EmailData() {
        // Constructors
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}
