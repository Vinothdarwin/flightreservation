package com.apm.demo.flight;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.apm.demo.controller.CustomerController;
import com.apm.demo.models.Booking;
import com.apm.demo.models.Customer;
import com.apm.demo.models.Flight;
import com.apm.demo.models.Role;
import com.apm.demo.repository.FlightRepository;
import com.apm.demo.service.BookingService;
import com.apm.demo.service.CustomerService;
import com.apm.demo.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

//@RunWith(MockitoJUnitRunner.class)
//@RunWith(SpringRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
public class CustomerControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockmvc;

	ObjectMapper objmap = new ObjectMapper();
	ObjectWriter objWriter = objmap.writer();

	@Autowired
	@InjectMocks
	private CustomerController cus_con;


	List<Booking> k = new ArrayList();

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		this.mockmvc = MockMvcBuilders.standaloneSetup(cus_con).build();
	}

	@Test
	public void showFlightsBasedOnSearch() throws Exception {

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		OngoingStubbing<Authentication> v = Mockito.when(securityContext.getAuthentication())
				.thenReturn(authentication); // MOCKING athentication obj
		OngoingStubbing<String> m = Mockito.when(authentication.getName()).thenReturn("Deepi");
		SecurityContextHolder.setContext(securityContext);

		ModelAndView mv = cus_con.showFlightsBasedOnSearch(
				new Flight((long) 4, "", "Chennai", "Delhi", "", (long) 3, 0, 0, "", k),
				new ExtendedModelMap());
		assertEquals(mv.getViewName(), "customer-flights");
		int length = ((ArrayList<Flight>) mv.getModel().get("flightList")).size(), i = 0;

		while (length > i) {
			assertEquals(((ArrayList<Flight>) mv.getModel().get("flightList")).get(i).getSource(), "Chennai");
			assertEquals(((ArrayList<Flight>) mv.getModel().get("flightList")).get(i).getDestination(), "Delhi");

			i++;
		}

		assertEquals(((Customer) mv.getModel().get("customer")).getUserName(), "deepi");

	}

	@Test
	public void showCustomerFlightsPage() throws Exception {
		
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication); // MOCKING athentication obj
		OngoingStubbing<String> m = Mockito.when(authentication.getName()).thenReturn("admin");
		SecurityContextHolder.setContext(securityContext);

		ModelAndView mv = cus_con.showCustomerFlightsPage(new ExtendedModelMap());
		assertEquals(mv.getViewName(), "admin-home");

		// USER LOGIN IN

		Mockito.when(authentication.getName()).thenReturn("hemalatha");
		SecurityContextHolder.setContext(securityContext);
		mv = cus_con.showCustomerFlightsPage(new ExtendedModelMap());
		assertEquals(mv.getViewName(), "customer-flights");
		Object flightlist = mv.getModelMap().getAttribute("flightList");
		assertEquals(((ArrayList<Flight>) flightlist).size(), 3);

		String customer = null;
		Mockito.when(authentication.getName()).thenReturn(customer);
		SecurityContextHolder.setContext(securityContext);

		// MOCKING A CUSTOMERSERVICE OBJECT

		mv = cus_con.showCustomerFlightsPage(new ExtendedModelMap());
		assertEquals(mv.getViewName(), "customer-flights");
		assertEquals(((ArrayList<Flight>) flightlist).size(), 3);

	}

}
