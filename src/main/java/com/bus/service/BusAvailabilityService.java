package com.bus.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bus.model.Bus;

@Service
public class BusAvailabilityService {

    // ===============================
    // MAIN METHOD
    // ===============================
    public String getAvailabilityLabel(Bus bus) {

        if (bus.getRunningDays() == null || bus.getRunningDays().isBlank()) {
            return "NOT SCHEDULED";
        }

        List<String> runningDays = Arrays.asList(bus.getRunningDays().split(","));

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        DayOfWeek tomorrow = today.plus(1);

        String todayCode = today.name().substring(0, 3);
        String tomorrowCode = tomorrow.name().substring(0, 3);

        // ✅ Runs Today
        if (runningDays.contains(todayCode)) {
            return "TODAY";
        }

        // ✅ Runs Tomorrow
        if (runningDays.contains(tomorrowCode)) {
            return "TOMORROW";
        }

        // ✅ Find next available day
        for (int i = 2; i <= 7; i++) {
            DayOfWeek next = today.plus(i);
            String code = next.name().substring(0, 3);

            if (runningDays.contains(code)) {
                return "NEXT " + code;
            }
        }

        return "NOT AVAILABLE";
    }
}
