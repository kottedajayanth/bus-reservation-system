package com.bus.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    // ✅ ENUM STATUS (Strong typing)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private String reservationType;
    private LocalDate reservationDate;
    private String reservationTime;
    private String source;
    private String destination;

    // ================= RELATIONS =================
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ================= JOURNEY FLAGS =================
    private boolean journeyStarted;
    private boolean journeyEnded;

    // ================= DERIVED STATUS =================
    @Transient
    public String getStatus() {
        if (journeyEnded)
            return "Ended";
        if (journeyStarted)
            return "Started";
        return "Not started";
    }

    // Optional journey timestamp
    private LocalDateTime journeyDate;

    // Number of seats booked
    private Integer seatsRequested;
}
