package com.apm.demo.service;

import com.apm.demo.models.Customer;
import com.apm.demo.models.Flight;
import com.apm.demo.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class FlightService {
    private static final Random random = new Random();

    private FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAll() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.getById(id);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteFlight(Flight flight) {
        flightRepository.delete(flight);
    }

    public int getFlightCount() {
        return getAll().size();
    }

    public Integer getAvailableSeats() {
        return flightRepository.getSeats();
    }

    public void deleteFlightById(Long id) {
        flightRepository.deleteById(id);
    }

    public Flight getRandomFlight() {
        List<Flight> flights = flightRepository.findAll();
        if (flights.isEmpty()) return null;
        return flights.get(random.nextInt(flights.size()));
    }

    public Flight getRandomFlight(Long start, Long end) {
        List<Flight> flights = flightRepository.findByTimeBetween(start, end);
        if (flights.isEmpty()) return null;
        return flights.get(random.nextInt(flights.size()));
    }

    public List<Flight> getFlightBetweenGivenRange(Long start, Long end) {
        return flightRepository.findByTimeBetween(start, end);
    }

    @Transactional
    public void deleteFlightsBefore(Long time){
        flightRepository.deleteByTimeBefore(time);
    }
}
