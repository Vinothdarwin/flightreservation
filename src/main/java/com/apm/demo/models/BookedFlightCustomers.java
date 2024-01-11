package com.apm.demo.models;

import java.util.ArrayList;
import java.util.List;

public class BookedFlightCustomers {
    private String flightId;
    private String flightName;
    private List<BookedCustomer> bookedCustomersDetails = new ArrayList<>();

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public List<BookedCustomer> getBookedCustomersDetails() {
        return bookedCustomersDetails;
    }

    public void setBookedCustomersDetails(List<BookedCustomer> bookedCustomersDetails) {
        this.bookedCustomersDetails = bookedCustomersDetails;
    }

    public BookedFlightCustomers(String flightId, String flightName, List<BookedCustomer> bookedCustomersDetails) {
        super();
        this.flightId = flightId;
        this.flightName = flightName;
        this.bookedCustomersDetails = bookedCustomersDetails;
    }

}
