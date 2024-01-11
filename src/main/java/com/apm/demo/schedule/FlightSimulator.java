package com.apm.demo.schedule;

import com.apm.demo.models.Flight;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FlightSimulator {
    private static final Logger log = LoggerFactory.getLogger(FlightSimulator.class);

    private static final String[] FLIGHTS = new String[]{"Emirates", "Air Asia", "Lufthansa", "Indigo", "SpiceJet","TruJet"};
    private static final String[] SOURCES = new String[]{"Chennai", "Mumbai", "Kolkata", "Delhi", "Jaipur"};
    private static final String[] DESTINATIONS = new String[]{"Hyderabad", "Bangalore", "Kochi", "Goa", "Pune"};
    private static final Integer[] FARE = new Integer[]{2000, 3000, 5000};
    private static final Integer[] SEATS = new Integer[]{200, 300, 150};
    private static final String[] IMAGES = new String[]{"img/emirates.png", "img/air asia.png", "img/lufthasan.png", "img/indigo.jpeg", "img/spicejet.jpeg","img/trujet.jpeg"};
    private static final Random RANDOMOBJ = new Random();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static List<Flight> getRandomFlights() {
        List<Flight> flightList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        String dateStr = SIMPLE_DATE_FORMAT.format(date);
        int flights = random(30);
        log.info("Inserting flights : " + flights);
        for (int i = 0; i < random(30); i++) {
            Flight flight = new Flight();
            int hash=hashCode(flight.getSource()+flight.getDestination());
            String flightName=getRandomElementFromArray(FLIGHTS);
            flight.setName(flightName+""+hash);;
            flight.setSource(getRandomElementFromArray(SOURCES));
            flight.setDestination(getRandomElementFromArray(DESTINATIONS));
            flight.setFare(getRandomElementFromArray(FARE));
            flight.setSeats(getRandomElementFromArray(SEATS));
            flight.setTime(date.getTime() + ThreadLocalRandom.current().nextLong(0, 60 * 60 * 1000));
            flight.setDate(dateStr);
            for(String str:IMAGES) {
            	if(str.contains(flightName.toLowerCase())) {
            		flight.setImage(str);
                    break;
            	}
            }
            
            flightList.add(flight);
        }
        return flightList;
    }

    public static String getRandomElementFromArray(String[] array) {
        return array[random(array.length)];
    }

    public static int getRandomElementFromArray(Integer[] array) {
        return array[random(array.length)];
    }

    static int random(int val) {
        return RANDOMOBJ.nextInt(val);
    }
    
    private static int hashCode(String ids) {
		int hash = Math.abs(ids.hashCode())%ids.length();
		return hash;
	}


}
