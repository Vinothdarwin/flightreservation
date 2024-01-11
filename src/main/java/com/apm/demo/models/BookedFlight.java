package com.apm.demo.models;

public class BookedFlight {
    private String bookingId;
    private String bookedFlightId;
    private int numOfTicketsBooked;
    private int totalPrice;
    private Flight bookedFlight;

    public BookedFlight(String id, String flightId, int ticketsBooked, int price, Flight flight) {
        bookingId = id;
        bookedFlightId = flightId;
        numOfTicketsBooked = ticketsBooked;
        totalPrice = price;
        bookedFlight = flight;
    }

    public int getNumOfTicketsBooked() {
        return numOfTicketsBooked;
    }

    public void setNumOfTicketsBooked(int numOfTicketsBooked) {
        this.numOfTicketsBooked = numOfTicketsBooked;
    }

    public String getBookedFlightId() {
        return bookedFlightId;
    }

    public void setBookedFlightId(String bookedFlightId) {
        this.bookedFlightId = bookedFlightId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Flight getBookedFlight() {
        return bookedFlight;
    }

    public void setBookedFlight(Flight bookedFlight) {
        this.bookedFlight = bookedFlight;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

}
