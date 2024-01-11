package com.apm.demo.redis.service;

import com.apm.demo.redis.entity.Seats;

public interface SeatsService {

    boolean saveSeats(Seats seats);

    void getAll();

    Seats getSeatsById(String id);
}
