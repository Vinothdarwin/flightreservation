package com.apm.demo.repository;

import com.apm.demo.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select sum(tickets) from Booking b")
    Integer getTicketCount();

    List<Booking> findByBookingTimeBetween(Long start, Long end);

    @Query(value = "select sleep(10)", nativeQuery = true)
    void sleep();

    void deleteByBookingTimeBefore(Long time);
}
