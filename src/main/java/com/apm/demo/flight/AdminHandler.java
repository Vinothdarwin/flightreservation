package com.apm.demo.flight;

import com.apm.demo.models.Flight;
import com.apm.demo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminHandler {

    @Autowired
    private FlightService flightService;

    @GetMapping("/admin/add_flight")
    public String addFlightForm2(Model model) {
        model.addAttribute("flight", new Flight());
        return "admin/add_flight";
    }

    @GetMapping("/admin_home")
    public String addFlightSubmit(Model model) {
        //Get Flight data from flight table and send it to the view
        List<Flight> flightList = new ArrayList<>();
        model.addAttribute("flightList", flightList);
        return "admin_home";
    }

    @PostMapping("/admin/add_flight")
    public String updateFlightDetails(@ModelAttribute Flight flight, Model model) {
        model.addAttribute("flight", flight);
        return "admin/add_flight";
    }
}
