package com.bus.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bus.dto.ReservationRequest;
import com.bus.model.Bus;
import com.bus.model.Reservation;
import com.bus.model.ReservationStatus;
import com.bus.model.User;
import com.bus.service.BusService;
import com.bus.service.ReservationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

	private final ReservationService reservationService;
	private final BusService busService;

	public ReservationController(ReservationService reservationService,
			BusService busService) {
		this.reservationService = reservationService;
		this.busService = busService;
	}

	// ================= LIST =================
	@GetMapping
	public String listReservations(Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return "redirect:/auth/login-user";
		}

		model.addAttribute("reservations",
				reservationService.findByUser(user));

		return "reservations/reservation-list";
	}

	// ================= FORM =================
	@GetMapping("/new")
	public String form(@RequestParam Long busId, Model model) {

		Bus bus = busService.get(busId);

		model.addAttribute("bus", bus);
		model.addAttribute("request", new ReservationRequest());

		return "reservations/reservation-form";
	}

	// ================= CREATE (OLD FLOW) =================
	@PostMapping
	public String create(@ModelAttribute ReservationRequest req) {

		Bus bus = busService.get(req.getBusId());

		Reservation r = new Reservation();
		r.setBus(bus);
		r.setReservationStatus(ReservationStatus.CONFIRMED);
		r.setReservationType(req.getReservationType());
		r.setReservationDate(LocalDate.now());
		r.setReservationTime(LocalTime.now().toString());
		r.setSource(req.getSource());
		r.setDestination(req.getDestination());

		reservationService.create(r);

		return "redirect:/reservations";
	}

	// ================= CANCEL =================
	@GetMapping("/{id}/cancel")
	public String cancel(@PathVariable Long id) {

		reservationService.cancel(id);

		return "redirect:/reservations";
	}

	// ================= PAYMENT PAGE =================
	@PostMapping("/payment")
	public String payment(@RequestParam Long busId,
			@RequestParam String date,
			@RequestParam String time,
			@RequestParam String source,
			@RequestParam String destination,
			@RequestParam(required = false) String seatsRequested,
			Model model) {

		LocalDate selectedDate = LocalDate.parse(date);
		LocalDate today = LocalDate.now();

		if (selectedDate.isBefore(today)) {
			model.addAttribute("error",
					"Past date booking is not allowed.");
			return "redirect:/buses";
		}

		LocalTime selectedTime = LocalTime.parse(time);
		LocalTime now = LocalTime.now();

		if (selectedDate.equals(today)
				&& selectedTime.isBefore(now)) {

			model.addAttribute("error",
					"Cannot book past time today.");
			return "redirect:/buses";
		}

		Bus bus = busService.get(busId);

		int passengers = Integer.parseInt(seatsRequested);

		double totalPrice = bus != null ? bus.getPrice() * passengers : 500;

		model.addAttribute("bus", bus);
		model.addAttribute("date", date);
		model.addAttribute("time", time);
		model.addAttribute("source", source);
		model.addAttribute("destination", destination);
		model.addAttribute("seatsRequested", seatsRequested);
		model.addAttribute("priceTotal", totalPrice);

		return "reservations/payment";
	}

	// ================= FINAL PAYMENT =================
	@PostMapping("/pay")
	public String pay(@RequestParam Long busId,
			@RequestParam String date,
			@RequestParam String time,
			@RequestParam String source,
			@RequestParam String destination,
			@RequestParam(required = false) String seatsRequested,
			@RequestParam String paymentMethod,
			@RequestParam String paymentIdentity,
			HttpSession session,
			Model model) {

		User user = (User) session.getAttribute("user");
		if (user == null)
			return "redirect:/auth/login-user";

		LocalDate selectedDate = LocalDate.parse(date);
		LocalDate today = LocalDate.now();

		if (selectedDate.isBefore(today)) {
			model.addAttribute("error",
					"Past date booking is not allowed.");
			return "redirect:/buses";
		}

		LocalTime selectedTime = LocalTime.parse(time);
		LocalTime now = LocalTime.now();

		if (selectedDate.equals(today)
				&& selectedTime.isBefore(now)) {

			model.addAttribute("error",
					"Cannot book past time today.");
			return "redirect:/buses";
		}

		Bus bus = busService.get(busId);

		boolean success = paymentMethod != null && !paymentMethod.isBlank();

		if (!success) {

			model.addAttribute("error",
					"Payment failed. Try again.");

			model.addAttribute("bus", bus);
			model.addAttribute("date", date);
			model.addAttribute("time", time);
			model.addAttribute("source", source);
			model.addAttribute("destination", destination);
			model.addAttribute("seatsRequested", seatsRequested);
			model.addAttribute("priceTotal", bus.getPrice());

			return "reservations/payment";
		}

		int passengers = Integer.parseInt(seatsRequested);

		Reservation r = reservationService.book(
				user,
				bus,
				LocalDate.parse(date),
				time,
				source,
				destination,
				passengers);

		model.addAttribute("reservation", r);
		model.addAttribute("user", user);

		return "reservations/reservation-details";
	}

	// ================= JOURNEY START =================
	@PostMapping("/start/{id}")
	public String startJourney(@PathVariable Long id) {

		Reservation res = reservationService.get(id);
		res.setJourneyStarted(true);

		reservationService.save(res);

		return "redirect:/reservations";
	}

	// ================= JOURNEY END =================
	@PostMapping("/end/{id}")
	public String endJourney(@PathVariable Long id) {

		Reservation res = reservationService.get(id);
		res.setJourneyEnded(true);

		reservationService.save(res);

		return "redirect:/feedback/new/" + id;
	}
}
