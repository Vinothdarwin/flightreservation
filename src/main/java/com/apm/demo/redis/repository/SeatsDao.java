package com.apm.demo.redis.repository;

import com.apm.demo.redis.entity.Seats;

public interface SeatsDao {
    boolean saveSeats(Seats seats);

    void getAll();

    Seats getSeatsById(String id);
}
