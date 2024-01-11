package com.apm.demo.redis.repository;

import com.apm.demo.redis.entity.Seats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatsDaoImpl implements SeatsDao {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    public static final String HASK_KEY = "Seats";

    public void deleteSeats(Long id) {
        template.opsForHash().delete(HASK_KEY, id);
    }

    @Override
    public boolean saveSeats(Seats seats) {
        template.opsForHash().put(HASK_KEY, seats.getId(), seats);
        return true;
    }

    @Override
    public void getAll() {
        template.opsForHash().get("1212121212", HASK_KEY);
        template.opsForHash().entries(HASK_KEY);
        template.opsForHash().entries(HASK_KEY).values();
    }

    @Override
    public Seats getSeatsById(String id) {
        return (Seats) template.opsForHash().get(HASK_KEY, id);
    }
}
