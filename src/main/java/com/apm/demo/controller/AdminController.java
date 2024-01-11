package com.apm.demo.controller;

import com.apm.demo.models.Booking;
import com.apm.demo.models.Customer;
import com.apm.demo.models.CustomerDetails;
import com.apm.demo.models.Flight;
import com.apm.demo.service.BookingService;
import com.apm.demo.service.CustomerService;
import com.apm.demo.service.FlightService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class AdminController {
	private static final Random random = new Random();

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	Logger logger = Logger.getLogger(LoginController.class.getName());

	@Autowired
	private CustomerService customerService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private BookingService bookingService;

	@GetMapping("/bookings/graph")
	public JSONObject getChartDataForAdmin() {
		JSONObject jsonObject = new JSONObject();
		long[] tw = getWeeksTw();
		jsonObject.put("graph", getBookingsGraph(tw));
		jsonObject.put("top_sources", new JSONObject(getTopSources()));
		jsonObject.put("top_destinations", new JSONObject(getTopDestinations()));
		return jsonObject;
	}

	@PostMapping("/admin/flights")
	public ModelAndView addFlightSubmit(@ModelAttribute Flight flight, Model model,
			@RequestParam("fileImage") MultipartFile multipartfle) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date date = calendar.getTime();

		logger.log(Level.SEVERE, "here at home update insert deletion");
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());

		if (user != null) {
			model.addAttribute("customer", user);
			if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
				modelAndView.setViewName("admin/flights");
				System.out.println(flight.getId());
				if (flight.getId() == null) {
					String fileName = StringUtils.cleanPath(multipartfle.getOriginalFilename());
					flight.setImage("img/" + fileName);
					Flight savedUser = flightService.saveFlight(flight);
					String uploadDir = "src/main/resources/static/img/";
					saveFile(uploadDir, fileName, multipartfle);

					int hash = hashCode(flight.getSource() + flight.getDestination());
					flight.setName(flight.getName() + "" + hash);
					flight.setTime(date.getTime() + ThreadLocalRandom.current().nextLong(0, 60 * 60 * 1000));
					flightService.saveFlight(flight);
				} else {
					if (flight.getName() != null) {
						/*
						 * When "ID" is not null, then this request is from flight update action. So
						 * delete the existing file from DB and insert updated one inorder to avoid
						 * duplicate entry exception.
						 */
						boolean isAlreadyBooked = checkForReferenceInBooking(flight.getId());
						if (!isAlreadyBooked) {
							flightService.deleteFlightById(flight.getId());
							System.out.println(flight + "");
							flightService.saveFlight(flight);

						}
					}
				}
				List<Flight> flightList = flightService.getAll();
				model.addAttribute("flightList", flightList);
				model.addAttribute("flightTemp", new Flight());
				setModelObjectForAdminDashboards(model);
			} else {
				modelAndView.setViewName("home");
			}
		} else {
			modelAndView.setViewName("login");
		}

		return modelAndView;
	}

	@PostMapping("/admin/delete")
	public ModelAndView deleteFlightSubmit(Model model, @ModelAttribute Flight flight) {
		logger.log(Level.SEVERE, "here at home update insert deletion");
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());

		if (user != null) {
			model.addAttribute("customer", user);
			if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
				modelAndView.setViewName("admin/flights");

				boolean isAlreadyBooked = checkForReferenceInBooking(flight.getId());
				if (!isAlreadyBooked) {
					flightService.deleteFlightById(flight.getId());
				}

				List<Flight> flightList = flightService.getAll();
				model.addAttribute("flightList", flightList);
				model.addAttribute("flightTemp", new Flight());
				setModelObjectForAdminDashboards(model);
			} else {
				modelAndView.setViewName("home");
			}
		} else {
			modelAndView.setViewName("login");
		}

		return modelAndView;

	}

	private int hashCode(String ids) {
		int hash = Math.abs(ids.hashCode()) % ids.length();
		return hash;
	}

	private boolean checkForReferenceInBooking(Long id) {
		List<Booking> bookings = bookingService.getAll();
		for (Booking booking : bookings) {
			if (booking.getFlight().getId() == id) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/admin/flights")
	public ModelAndView adminHome(Model model) {
		logger.log(Level.SEVERE, "here at home");
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		if (user != null) {
			model.addAttribute("customer", user);
			if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
				modelAndView.setViewName("admin/flights");
				List<Flight> flightList = flightService.getAll();
				model.addAttribute("flightList", flightList);
				model.addAttribute("flightTemp", new Flight());
				setModelObjectForAdminDashboards(model);
			} else {
				modelAndView.setViewName("home");
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@GetMapping("/admin/customers")
	public ModelAndView adminCustomers(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer user = customerService.findUserByUserName(auth.getName());
		if (user != null) {
			model.addAttribute("customer", user);
			if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"))) {
				modelAndView.setViewName("admin/customers");
				setModelObjectForAdminDashboards(model);
			} else {
				modelAndView.setViewName("home");
			}
		} else {
			modelAndView.setViewName("login");
		}

		List<Booking> bookings = bookingService.getAll();
		List<CustomerDetails> customerDetails = new ArrayList<>();
		System.out.println(bookings.size());
		for (Booking bok : bookings) {
			CustomerDetails cd = new CustomerDetails(bok.getCustomer().getId(), bok.getCustomer().getUserName(),
					bok.getCustomer().getName(), bok.getCustomer().getLastName(), bok.getCustomer().getEmail(),
					bok.getFlight().getName(), bok.getFlight().getTime(), bok.getFlight().getDate(),
					bok.getFlight().getSource(), bok.getFlight().getDestination(), bok.getTickets(),
					bok.getFlight().getSeats() * bok.getTickets());

			customerDetails.add(cd);
		}
		model.addAttribute("users", customerDetails);

		return modelAndView;
	}

	private JSONObject getBookingsGraph(long[] tw) {
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
		return new JSONObject(dateVsCount);
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

	public void setModelObjectForAdminDashboards(Model model) {
		model.addAttribute("total_flights", flightService.getFlightCount());
		model.addAttribute("total_customers", customerService.getCustomerCount());
		Integer booked = bookingService.getBookedTickets();
		booked = booked == null || booked == 0 ? 1 : booked;

		Integer total = flightService.getAvailableSeats();
		model.addAttribute("booked_tickets", booked);
		if (total == null) {
			model.addAttribute("vacancy", 0);
			model.addAttribute("available_seats", 0);
		} else {
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
	
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
        	Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }
}
