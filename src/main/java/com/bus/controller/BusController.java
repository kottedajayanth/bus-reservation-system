package com.bus.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bus.model.Bus;
import com.bus.model.Route;
import com.bus.service.BusService;
import com.bus.service.RouteService;

@Controller
@RequestMapping("/buses")
public class BusController {

	private final BusService busService;
	private final RouteService routeService;

	public BusController(BusService busService, RouteService routeService) {
		this.busService = busService;
		this.routeService = routeService;
	}

	// ================= LIST BUSES =================
	@GetMapping
	public String list(Model model,
			@RequestParam(required = false) String from,
			@RequestParam(required = false) String to) {

		List<Bus> buses = (from != null && to != null && !from.isBlank() && !to.isBlank())
				? busService.searchByRoute(from, to)
				: busService.listAll();

		model.addAttribute("buses", buses);

		// ===== BOSS STEP-3 : RUNNING STATUS =====
		Map<Long, String> runningStatus = new HashMap<>();

		DayOfWeek today = LocalDate.now().getDayOfWeek();
		DayOfWeek tomorrow = today.plus(1);

		for (Bus b : buses) {

			String days = b.getRunningDays();

			if (days == null || days.isBlank())
				continue;

			String todayCode = today.name().substring(0, 3);
			String tomorrowCode = tomorrow.name().substring(0, 3);

			if (days.contains(todayCode)) {
				runningStatus.put(b.getBusId(), "TODAY");
			} else if (days.contains(tomorrowCode)) {
				runningStatus.put(b.getBusId(), "TOMORROW");
			} else {
				String next = days.split(",")[0];
				runningStatus.put(b.getBusId(), "NEXT " + next);
			}
		}

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
