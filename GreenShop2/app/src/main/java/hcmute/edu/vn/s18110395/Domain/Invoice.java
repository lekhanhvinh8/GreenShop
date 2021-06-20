package hcmute.edu.vn.s18110395.Domain;

import java.util.ArrayList;

public class Invoice {
    private int orderId;
    private int userId;
    private String date;

    public ArrayList<Product> products;

    public Invoice(){
        products = new ArrayList<Product>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
