package com.apm.demo.controller;

import com.apm.demo.models.Booking;
import com.apm.demo.models.Customer;
import com.apm.demo.models.Flight;
import com.apm.demo.redis.entity.Seats;
import com.apm.demo.redis.service.SeatsService;
import com.apm.demo.service.BookingService;
import com.apm.demo.service.CustomerService;
import com.apm.demo.service.FlightService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	private Logger logger = Logger.getLogger(LoginController.class.getName());

	@Autowired
	private CustomerService customerService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private SeatsService seatsService;

	@Value("${spring.payment-service.url}")
	private String paymentServiceUrl;

	@PostMapping("/customer-flights")
	public ModelAndView showFlightsBasedOnSearch(@ModelAttribute Flight flight, Model model) {
		String source = flight.getSource();
		String destination = flight.getDestination();
		List<Flight> flights = flightService.getAll();
		List<Flight> flightsForView = new ArrayList<>();
		for (Flight f : flights) {
			if (f.getSource().equalsIgnoreCase(source) && f.getDestination().equalsIgnoreCase(destination)) {
				flightsForView.add(f);
			}
		}
		if (flightsForView.isEmpty()) {
			flightsForView = null;
		}
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		model.addAttribute("customer", user);
		model.addAttribute("flightList", flightsForView);
		modelAndView.setViewName("customer-flights");
		return modelAndView;
	}

	@GetMapping("/customer-flights")
	public ModelAndView showCustomerFlightsPage(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		if (user != null) {
			model.addAttribute("customer", user);
			if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
				modelAndView.setViewName("admin-home");
				setModelObjectForAdminDashboards(model);
			} else {
				List<Flight> flightList = flightService.getAll();
				model.addAttribute("flightList", flightList);
				model.addAttribute("flightTemp", new Flight());
				modelAndView.setViewName("customer-flights");
			}
		} else {
			List<Flight> flightList = flightService.getAll();
			model.addAttribute("flightList", flightList);
			model.addAttribute("flightTemp", new Flight());
			modelAndView.setViewName("customer-flights");
		}
		return modelAndView;
	}

	@PostMapping("/book-flight")
	public ModelAndView bookFlight(@ModelAttribute Flight flightTemp, Model model) {

		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		if (user == null) {
			modelAndView.setViewName("login");

		} else {
			Flight flight1 = flightService.getFlightById(flightTemp.getId());
			Booking details = new Booking();
			details.setFlight(flight1);
			model.addAttribute("flight", flight1);
			model.addAttribute("bookings", details);
			modelAndView.setViewName("book-flight");
		}
		return modelAndView;
	}

	@PostMapping(value = "/cancel-flight")
	public ModelAndView cancelFlight(@ModelAttribute Flight flightTemp, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		if (user == null) {
			modelAndView.setViewName("login");

		} else {
			model.addAttribute("customer", user);
			List<Booking> bookings = bookingService.getAll();
			Long bookid = null;
			for (Booking b : bookings) {
				if (b.getFlight().getId().equals(flightTemp.getId()) && b.getCustomer().getId().equals(user.getId())) {
					b.getFlight().setSeats(b.getFlight().getSeats() + b.getTickets());
					flightService.saveFlight(b.getFlight());
					bookid = b.getId();
					bookingService.deleteBooking(bookid);
				}

			}
			List<Flight> flightListForView = new ArrayList<>();
			List<Booking> booking = bookingService.getAll();
			for (Booking b : booking) {
				if (b.getCustomer().getId() == user.getId()) {
					int flightFare = b.getFlight().getFare();
					int seats = b.getTickets();
					flightFare = flightFare * b.getTickets();
					Flight tempFlight = b.getFlight();
					tempFlight.setSeats(seats);
					tempFlight.setFare(flightFare);
					flightListForView.add(tempFlight);
				}
			}
			if (flightListForView.isEmpty()) {
				modelAndView.setViewName("customer-bookings-empty");
			} else {
				model.addAttribute("flightList", flightListForView);
				model.addAttribute("bookings", booking);
				// Used as temp variable for cancel booking
				model.addAttribute("booking", new Booking());
				modelAndView.setViewName("customer-bookings");
			}

		}

		return modelAndView;
	}

	@PostMapping("/submit_booking")
	public ModelAndView addFlightSubmit(@ModelAttribute Booking booking, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		booking.setBookingTime(System.currentTimeMillis());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		booking.setCustomer(user);
		if (booking.getTickets() > 0 && booking.getTickets() <= booking.getFlight().getSeats()) {

			Flight flight = booking.getFlight();
			model.addAttribute("flight", flight);
			model.addAttribute("booking", booking);
			modelAndView.setViewName("payment-portal");
		} else {
			Flight flight1 = booking.getFlight();
			Booking details = new Booking();
			details.setFlight(flight1);
			model.addAttribute("flight", flight1);
			model.addAttribute("bookings", details);
			modelAndView.setViewName("book-flight");
		}

		return modelAndView;
	}

	@PostMapping("/payment")
	public ModelAndView payment(@ModelAttribute Booking booking, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		booking.setBookingTime(System.currentTimeMillis());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		Booking booked = bookingService.saveBooking(booking);
		booking.getFlight().setSeats(booking.getFlight().getSeats() - booking.getTickets());
		flightService.saveFlight(booking.getFlight());
		List<Flight> flightList = flightService.getAll();
		model.addAttribute("customer", user);
		model.addAttribute("flightList", flightList);
		model.addAttribute("flightTemp", new Flight());
		modelAndView.setViewName("customer-home");
		verifyPayment(booked);
		return modelAndView;
	}

	@GetMapping("/customer-bookings")
	public ModelAndView showCustomerBookings(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		if (user != null) {
			model.addAttribute("customer", user);
			if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
				modelAndView.setViewName("admin-home");
				setModelObjectForAdminDashboards(model);
			} else {
				List<Booking> bookings = bookingService.getAll();
				List<Flight> flightListForView = new ArrayList<>();
				for (Booking b : bookings) {
					if (b.getCustomer().getId() == user.getId()) {
						System.out.println(user.getName());
						int flightFare = b.getFlight().getFare();
						int seats = b.getTickets();
						flightFare = flightFare * b.getTickets();
						Flight tempFlight = b.getFlight();
						tempFlight.setSeats(seats);
						tempFlight.setFare(flightFare);
						flightListForView.add(tempFlight);
					}
				}
				if (flightListForView.isEmpty()) {
					modelAndView.setViewName("customer-bookings-empty");
				} else {
					model.addAttribute("flightList", flightListForView);
					model.addAttribute("bookings", bookings);
					// Used as temp variable for cancel booking
					model.addAttribute("booking", new Booking());
					modelAndView.setViewName("customer-bookings");
				}

			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	private void verifyPayment(Booking booking) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("booking_id", booking.getId());
		jsonObject.put("type", "card");
		HttpEntity<JSONObject> request = new HttpEntity<>(jsonObject, headers);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject(paymentServiceUrl, request, String.class);
	}

	private void setModelObjectForAdminDashboards(Model model) {
		model.addAttribute("total_flights", flightService.getFlightCount());
		model.addAttribute("total_customers", customerService.getCustomerCount());
		Integer booked = bookingService.getBookedTickets();
		booked = booked == null || booked == 0 ? 1 : booked;
		int total = flightService.getAvailableSeats();
		model.addAttribute("booked_tickets", booked);
		model.addAttribute("vacancy", booked / total);
		model.addAttribute("available_seats", total);
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
		return new long[] { start, end };
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
		Map<String, Integer> sortedMap = topSources.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
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
		Map<String, Integer> sortedMap = topDestinations.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return sortedMap;
	}
}
