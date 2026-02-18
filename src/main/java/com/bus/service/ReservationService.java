package com.bus.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bus.exception.ResourceNotFoundException;
import com.bus.model.Bus;
import com.bus.model.Reservation;
import com.bus.model.ReservationStatus;
import com.bus.model.User;
import com.bus.repository.BusRepository;
import com.bus.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {

	private final ReservationRepository reservationRepo;
	private final BusRepository busRepo;

	public ReservationService(ReservationRepository reservationRepo,
			BusRepository busRepo) {
		this.reservationRepo = reservationRepo;
		this.busRepo = busRepo;
	}

	// ================= LIST =================
	public List<Reservation> listAll() {
		return reservationRepo.findAll();
	}

	public Reservation create(Reservation r) {
		return reservationRepo.save(r);
	}

	// ================= BOOK =================
	@Transactional
	public Reservation book(User user,
			Bus bus,
			LocalDate date,
			String time,
			String source,
			String destination,
			int seatsRequested) {

		Bus b = busRepo.findById(bus.getBusId())
				.orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

		// Seat validation
		if (b.getAvailableSeats() == null ||
				b.getAvailableSeats() < seatsRequested) {
			throw new RuntimeException("Not enough seats available");
		}

		// Reduce available seats
		b.setAvailableSeats(
				b.getAvailableSeats() - seatsRequested);
		busRepo.save(b);

		// Create reservation
		Reservation r = new Reservation();
		r.setBus(b);
		r.setUser(user);
		r.setReservationStatus(ReservationStatus.CONFIRMED);
		r.setReservationType("ONLINE");
		r.setReservationDate(date);
		r.setReservationTime(time);
		r.setSource(source);
		r.setDestination(destination);
		r.setSeatsRequested(seatsRequested);

		return reservationRepo.save(r);
	}

	// ================= CANCEL =================
	@Transactional
	public void cancel(Long id) {

		Reservation r = reservationRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

		// Prevent double cancellation
		if (r.getReservationStatus() == ReservationStatus.CANCELLED) {
			throw new RuntimeException("Reservation already cancelled");
		}

		// Prevent cancel after journey started
		if (r.isJourneyStarted()) {
			throw new RuntimeException(
					"Cannot cancel after journey has started");
		}

		// Restore seats
		Bus b = r.getBus();

		if (b != null && b.getAvailableSeats() != null) {

			int seatsToRestore = r.getSeatsRequested() != null
					? r.getSeatsRequested()
					: 1;

			b.setAvailableSeats(
					b.getAvailableSeats() + seatsToRestore);

			busRepo.save(b);
		}

		// Update reservation status
		r.setReservationStatus(ReservationStatus.CANCELLED);
		reservationRepo.save(r);
	}

	// ================= USER RESERVATIONS =================
	public List<Reservation> findByUser(User user) {
		return reservationRepo.findByUser(user);
	}

	// ================= GET =================
	public Reservation get(Long id) {
		return reservationRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Reservation not found: " + id));
	}

	public void save(Reservation reservation) {
		reservationRepo.save(reservation);
	}

	// ================= FIND BY BUS =================
	public List<Reservation> findByBus(Long busId) {
		return reservationRepo.findByBusBusId(busId);
	}
}
