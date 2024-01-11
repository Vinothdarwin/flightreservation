package com.apm.demo.models;

public class BookedCustomer {
    private String customerId;
    private String customerName;
    private int numOfTicketsBooked;

    public BookedCustomer(String customerId, String customerName, int numOfTicketsBooked) {
        super();
        this.customerId = customerId;
        this.customerName = customerName;
        this.numOfTicketsBooked = numOfTicketsBooked;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getNumOfTicketsBooked() {
        return numOfTicketsBooked;
    }

    public void setNumOfTicketsBooked(int numOfTicketsBooked) {
        this.numOfTicketsBooked = numOfTicketsBooked;
    }

}
