package com.apm.demo.service;

import com.apm.demo.models.Booking;
import com.apm.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
public void delete(Iterable<? extends Booking> id) {
	
}
    
    public int getBookingCount() {
        return bookingRepository.findAll().size();
    }

    public Integer getBookedTickets() {
        return bookingRepository.getTicketCount();
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void sleep() {
        bookingRepository.sleep();
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsForTime(Long start, Long end) {
        return bookingRepository.findByBookingTimeBetween(start, end);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public void deleteByBookingTimeBefore(Long time){
        bookingRepository.deleteByBookingTimeBefore(time);
    }
}
