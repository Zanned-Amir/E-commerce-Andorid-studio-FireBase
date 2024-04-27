package com.sph.sbh.Model;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private long timestamp;
    private ArrayList<Modif> items;

    public Order() {

    }

    public Order(String orderId, long timestamp, ArrayList<Modif> items) {
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Modif> getItems() {
        return items;
    }

    public void setItems(ArrayList<Modif> items) {
        this.items = items;
    }


}

