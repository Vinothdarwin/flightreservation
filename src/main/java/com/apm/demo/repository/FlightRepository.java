package com.apm.demo.repository;

import com.apm.demo.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findAll();

    @Query("select sum(seats) from Flight f")
    Integer getSeats();

    List<Flight> findByTimeBetween(Long start, Long end);
    void deleteByTimeBefore(Long time);

}
