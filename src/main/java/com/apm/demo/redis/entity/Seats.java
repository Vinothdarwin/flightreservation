package com.apm.demo.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class Seats implements Serializable {

    private String id;
    private String seats;


}
