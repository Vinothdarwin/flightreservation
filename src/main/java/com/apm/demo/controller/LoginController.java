package com.apm.demo.controller;

import com.apm.demo.models.Booking;
import com.apm.demo.models.Customer;
import com.apm.demo.models.Flight;
import com.apm.demo.redis.service.SeatsService;
import com.apm.demo.service.BookingService;
import com.apm.demo.service.CustomerService;
import com.apm.demo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SeatsService seatsService;

    @GetMapping(value = {"/login"})
    public ModelAndView login(@RequestParam(value = "error",required = false) Boolean error) {
        ModelAndView modelAndView = new ModelAndView();
        if(error!=null && error){
            modelAndView.addObject("error","Invalid credentials");
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping(value = "/login")
    public ModelAndView login(@Valid Customer user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Customer userExists = customerService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            customerService.saveUser(user);
        } else {
            bindingResult
                    .rejectValue("InvalidUser", "error.user",
                            "Invalid User");
        }
        return modelAndView;
    }
    
    @GetMapping(value="/user_profile")
    public ModelAndView profile(Model model) {
    	ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer user = customerService.findUserByUserName(auth.getName());
        System.err.println(user.getName()+" "+user.getEmail());
        model.addAttribute("user",user);
        modelAndView.setViewName("user_profile");
        return modelAndView;
    }
    
    
    @GetMapping(value = "/registration")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        Customer user = new Customer();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid Customer user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Customer userExists = customerService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("error","user already exists");
            modelAndView.addObject("user", user);
            modelAndView.setViewName("registration");
        } else {
            customerService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", user);
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping(value = {"/","/home"})
    public ModelAndView showHomePage(Model model) {
        logger.log(Level.SEVERE, "here at home");
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer user = customerService.findUserByUserName(auth.getName());
        if (user != null) {
            model.addAttribute("customer", user);
            if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
                modelAndView.setViewName("admin-home");
                setModelObjectForAdminDashboards(model);
            } else {
                List<Flight> flightList = flightService.getFlightBetweenGivenRange(System.currentTimeMillis(), System.currentTimeMillis() + 60 * 60 * 50000);
                if (flightList.isEmpty()) {
                    flightList = flightService.getAll();
                }
                model.addAttribute("flightList", flightList);
                model.addAttribute("flightTemp", new Flight());
                modelAndView.setViewName("customer-home");
            }
        } else {
            List<Flight> flightList = flightService.getFlightBetweenGivenRange(System.currentTimeMillis(), System.currentTimeMillis() + 60 * 60 * 50000);
            if (flightList.isEmpty()) {
                flightList = flightService.getAll();
            }
            model.addAttribute("flightList", flightList);
            model.addAttribute("flightTemp", new Flight());
            modelAndView.setViewName("customer-home");
        }
        return modelAndView;
    }

    private void setModelObjectForAdminDashboards(Model model) {
        model.addAttribute("total_flights", flightService.getFlightCount());
        model.addAttribute("total_customers", customerService.getCustomerCount());
        Integer booked = bookingService.getBookedTickets();
        booked = booked == null || booked == 0 ? 1 : booked;

        Integer total = flightService.getAvailableSeats();
        model.addAttribute("booked_tickets", booked);
        if(total == null){
            model.addAttribute("vacancy", 0);
            model.addAttribute("available_seats", 0);
        }else{
            model.addAttribute("vacancy", booked / total);
            model.addAttribute("available_seats", total);
        }
        getChartDataForAdmin(model);
    }

    private void getChartDataForAdmin(Model model) {
        long[] tw = getWeeksTw();
        getBookingsGraph(tw);
        logger.log(Level.SEVERE, getTopSources().toString());
        logger.log(Level.SEVERE, getTopDestinations().toString());
    }

   private long[] getWeeksTw() {
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

    private Map<String, Integer> getBookingsGraph(long[] tw) {
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

    private Map<String, Integer> getTopSources() {
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

    private Map<String, Integer> getTopDestinations() {
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