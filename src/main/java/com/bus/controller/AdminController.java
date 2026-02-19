package com.bus.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bus.model.Bus;
import com.bus.model.Route;
import com.bus.service.BusService;
import com.bus.service.FeedbackService;
import com.bus.service.RouteService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final BusService busService;
	private final RouteService routeService;
	private final FeedbackService feedbackService;

	public AdminController(BusService busService,
			FeedbackService feedbackService,
			RouteService routeService) {

		this.busService = busService;
		this.routeService = routeService;
		this.feedbackService = feedbackService;
	}

	// ================= DASHBOARD =================
	@GetMapping
	public String dashboard(HttpSession session, Model model) {

		if (session.getAttribute("admin") == null)
			return "redirect:/auth/login-admin";

		model.addAttribute("busCount", busService.listAll().size());
		model.addAttribute("routeCount", routeService.listAll().size());
		model.addAttribute("buses", busService.listAll());
		model.addAttribute("routes", routeService.listAll());
		model.addAttribute("feedbackCount", feedbackService.listAll().size());

		return "admin/admin-dashboard";
	}

	// ================= BUS LIST =================
	@GetMapping("/buses")
	public String adminBuses(Model model) {
		model.addAttribute("buses", busService.listAll());
		return "admin/buses/bus-list";
	}

	// ================= NEW BUS FORM =================
	@GetMapping("/buses/new")
	public String adminBusForm(Model model) {
		model.addAttribute("bus", new Bus());
		model.addAttribute("routes", routeService.listAll());
		return "admin/buses/bus-form";
	}

	// ================= CREATE BUS (BOSS ENABLED) =================
	@PostMapping("/buses")
	public String adminBusCreate(
			@ModelAttribute Bus bus,
			@RequestParam Long routeId,
			@RequestParam(required = false) List<String> runningDays) {

		Route route = routeService.get(routeId);
		bus.setRoute(route);

		// ✅ BOSS LOGIC
		if (runningDays != null && !runningDays.isEmpty()) {
			bus.setRunningDays(String.join(",", runningDays));
		}

		busService.save(bus);

		return "redirect:/admin/buses";
	}

	// ================= EDIT FORM =================
	@GetMapping("/buses/edit/{id}")
	public String editBusForm(@PathVariable Long id, Model model) {

		Bus bus = busService.get(id);

		model.addAttribute("bus", bus);
		model.addAttribute("routes", routeService.listAll());

		return "admin/buses/bus-edit-form";
	}

	// ================= UPDATE BUS (BOSS ENABLED) =================
	@PostMapping("/buses/update/{id}")
	public String updateBus(
			@PathVariable Long id,
			@ModelAttribute Bus bus,
			@RequestParam Long routeId,
			@RequestParam(required = false) List<String> runningDays) {

		Route route = routeService.get(routeId);
		bus.setRoute(route);
		bus.setBusId(id);

		// ✅ BOSS LOGIC
		if (runningDays != null && !runningDays.isEmpty()) {
			bus.setRunningDays(String.join(",", runningDays));
		}

		busService.save(bus);

		return "redirect:/admin/buses";
	}

	// ================= DELETE BUS =================
	@PostMapping("/buses/delete/{id}")
	public String deleteBus(@PathVariable Long id) {
		busService.delete(id);
		return "redirect:/admin/buses";
	}

	// ================= ROUTES =================
	@GetMapping("/routes")
	public String adminRoutes(Model model) {
		model.addAttribute("routes", routeService.listAll());
		return "admin/routes/route-list";
	}

	@GetMapping("/routes/new")
	public String adminRouteForm(Model model) {
		model.addAttribute("route", new Route());
		return "admin/routes/route-form";
	}

	@PostMapping("/routes")
	public String adminRouteCreate(@ModelAttribute Route route) {
		routeService.save(route);
		return "redirect:/admin/routes";
	}

	@GetMapping("/routes/edit/{id}")
	public String editRouteForm(@PathVariable Long id, Model model) {
		Route route = routeService.get(id);
		model.addAttribute("route", route);
		return "admin/routes/route-edit-form";
	}

	@PostMapping("/routes/update/{id}")
	public String updateRoute(@PathVariable Long id,
			@ModelAttribute Route route) {

		route.setRouteId(id);
		routeService.save(route);

		return "redirect:/admin/routes";
	}

	@PostMapping("/routes/delete/{id}")
	public String deleteRoute(@PathVariable Long id) {
		routeService.delete(id);
		return "redirect:/admin/routes";
	}
}
