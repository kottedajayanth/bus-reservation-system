package com.bus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bus.model.Bus;
import com.bus.model.Route;
import com.bus.service.BusAvailabilityService;
import com.bus.service.BusService;
import com.bus.service.RouteService;

@Controller
@RequestMapping("/buses")
public class BusController {

	private final BusService busService;
	private final RouteService routeService;
	private final BusAvailabilityService availabilityService;

	// ================= CONSTRUCTOR =================
	public BusController(
			BusService busService,
			RouteService routeService,
			BusAvailabilityService availabilityService) {

		this.busService = busService;
		this.routeService = routeService;
		this.availabilityService = availabilityService;
	}

	// ================= LIST BUSES =================
	@GetMapping
	public String list(Model model,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to) {

		List<Bus> buses = (from != null && to != null && !from.isBlank() && !to.isBlank())
				? busService.searchByRoute(from, to)
				: busService.listAll();

		// ===== SBSS STEP-1 : Availability Engine =====
		Map<Long, String> runningStatus = new HashMap<>();

		for (Bus bus : buses) {
			String label = availabilityService.getAvailabilityLabel(bus);
			runningStatus.put(bus.getBusId(), label);
		}

		model.addAttribute("buses", buses);
		model.addAttribute("runningStatus", runningStatus);

		return "buses/bus-list";
	}

	// ================= CREATE FORM =================
	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("bus", new Bus());
		model.addAttribute("routes", routeService.listAll());
		return "buses/bus-form";
	}

	// ================= CREATE BUS =================
	@PostMapping
	public String create(@ModelAttribute Bus bus,
			@RequestParam Long routeId) {

		Route route = routeService.get(routeId);
		bus.setRoute(route);

		busService.save(bus);

		return "redirect:/buses";
	}

	// ================= DELETE BUS =================
	@GetMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		busService.delete(id);
		return "redirect:/buses";
	}
}
