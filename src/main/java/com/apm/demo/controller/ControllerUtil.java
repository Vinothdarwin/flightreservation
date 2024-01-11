package com.apm.demo.controller;

import com.apm.demo.models.Booking;
import com.apm.demo.service.BookingService;
import com.apm.demo.service.CustomerService;
import com.apm.demo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RestController
public class ControllerUtil {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private static Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private static CustomerService customerService;
    @Autowired
    private static FlightService flightService;
    @Autowired
    private static BookingService bookingService;



    public static void getChartDataForAdmin(Model model) {
        long[] tw = getWeeksTw();
        getBookingsGraph(tw);
        logger.log(Level.SEVERE, getTopSources().toString());
        logger.log(Level.SEVERE, getTopDestinations().toString());
    }

    public static long[] getWeeksTw() {
        Calendar calendar = Calendar.getInstance();
        Long end = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Long start = calendar.getTimeInMillis();
        return new long[]{start, end};
    }

    public static Map<String, Integer> getBookingsGraph(long[] tw) {
        List<Booking> bookings = bookingService.getBookingsForTime(tw[0], tw[1]);
        Map<String, Integer> dateVsCount = new HashMap<>();
        bookings.forEach(booking -> {
            Date date = new Date(booking.getBookingTime());
            String dateStr = SIMPLE_DATE_FORMAT.format(date);
            if (dateVsCount.containsKey(dateStr)) {
                dateVsCount.put(dateStr, dateVsCount.get(dateStr) + 1);
            } else {
                dateVsCount.put(dateStr, 1);
            }
        });
        return dateVsCount;
    }

    public static Map<String, Integer> getTopSources() {
        Map<String, Integer> topSources = new HashMap<>();
        List<Booking> bookings = bookingService.getAll();
        bookings.forEach(booking -> {
            String source = booking.getFlight().getSource();
            Integer tickets = booking.getTickets();
            if (topSources.containsKey(source)) {
                topSources.put(source, topSources.get(source) + tickets);
            } else {
                topSources.put(source, tickets);
            }
        });
        Map<String, Integer> sortedMap =
                topSources.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
        return sortedMap;
    }

    public static Map<String, Integer> getTopDestinations() {
        Map<String, Integer> topDestinations = new HashMap<>();
        List<Booking> bookings = bookingService.getAll();
        bookings.forEach(booking -> {
            String source = booking.getFlight().getDestination();
            Integer tickets = booking.getTickets();
            if (topDestinations.containsKey(source)) {
                topDestinations.put(source, topDestinations.get(source) + tickets);
            } else {
                topDestinations.put(source, tickets);
            }
        });
        Map<String, Integer> sortedMap =
                topDestinations.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
        return sortedMap;
    }


}
