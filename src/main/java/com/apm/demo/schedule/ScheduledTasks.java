package com.apm.demo.schedule;

import com.apm.demo.models.Booking;
import com.apm.demo.models.Customer;
import com.apm.demo.models.Flight;
import com.apm.demo.redis.entity.Seats;
import com.apm.demo.redis.service.SeatsService;
import com.apm.demo.resttemplate.PaymentRestClient;
import com.apm.demo.service.BookingService;
import com.apm.demo.service.CustomerService;
import com.apm.demo.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Random;

@Component
public class ScheduledTasks {

    private static final Random random = new Random();

    @Autowired
    private FlightService flightService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatsService seatsService;

    @Value("${spring.payment-service.url}")
    private String paymentServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //every hour
    @Scheduled(cron = "0 */5 * * * *")
    public void simulateFlights() {
        FlightSimulator.getRandomFlights().forEach(flight -> {
            log.info(flight.toString());
            flightService.saveFlight(flight);
        });
    }

    //every hour
    @Scheduled(cron = "0 0 * * * *")
    public void simulateRedisGet() {
        try {
            for (int i = 0; i < 100; i++) {
                Seats s = seatsService.getSeatsById("121212121235");
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    //every hour
//    @Scheduled(cron = "0 0 * * * *")
    public void simulateMemorySpike() {
        try {
            MemoryException.start();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    //every two hour
    @Scheduled(cron = "0 0 */2 * * *")
    public void simulateDbSleep() {
        try {
            bookingService.sleep();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    //every two hour
    @Scheduled(cron = "0 0 */2 * * *")
    public void simulateDbCalls() {
        try {
            for(int i=0;i<100;i++) {
                bookingService.getAll();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    //every ten minutes
    @Scheduled(cron = "0 */10 * * * *")
    public void simulateBookings() {
        for (int i = 0; i < random.nextInt(5) + 1; i++) {
            Flight flight = flightService.getRandomFlight(System.currentTimeMillis() - 60 * 60 * 10000, System.currentTimeMillis());
            Customer customer = customerService.getRandomCustomer();
            if (customer != null && flight != null) {
                Booking booking = new Booking();
                booking.setTickets(random.nextInt(10) + 1);
                if ((random.nextInt(10) + 1) / 5 == 1) {
                    bookingService.sleep();
                }
                booking.setCustomer(customer);
                booking.setFlight(flight);
                long randomMinuteOffset = random.nextInt(60) * 60 * 1000;
                booking.setBookingTime(System.currentTimeMillis() - randomMinuteOffset);
                try {
                    verifyPayment(bookingService.saveBooking(booking));
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void deleteOlderData(){
        log.error("deleting older data");
        long ms = System.currentTimeMillis() - 60*60*1000l;
        log.error(String.valueOf(ms));
        flightService.deleteFlightsBefore(ms); //deleting data 30 days older
        bookingService.deleteByBookingTimeBefore(ms);
    }

    private void verifyPayment(Booking booking) {
        PaymentRestClient client = new PaymentRestClient(restTemplate);
        client.postForEntity(paymentServiceUrl, booking);
    }
}