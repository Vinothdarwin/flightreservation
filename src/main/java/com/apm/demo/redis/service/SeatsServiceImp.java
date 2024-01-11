package com.apm.demo.redis.service;

import com.apm.demo.redis.entity.Seats;
import com.apm.demo.redis.repository.SeatsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatsServiceImp implements SeatsService {

    @Autowired
    private SeatsDao seatsDao;

    @Override
    public boolean saveSeats(Seats seats) {
        return seatsDao.saveSeats(seats);
    }

    @Override
    public void getAll() {
        seatsDao.getAll();
    }

    @Override
    public Seats getSeatsById(String id) {
        return seatsDao.getSeatsById(id);
    }
}
