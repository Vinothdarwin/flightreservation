package com.apm.demo.models;

public class BookingDetails {
    private String customerId;
    private String flightId;
    private int numberOfSeat;
    private boolean isMealRequired;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public int getNumberOfSeat() {
        return numberOfSeat;
    }

    public void setNumberOfSeat(int numberOfSeat) {
        this.numberOfSeat = numberOfSeat;
    }

    public boolean isMealRequired() {
        return isMealRequired;
    }

    public void setMealRequired(boolean mealRequired) {
        isMealRequired = mealRequired;
    }
}
