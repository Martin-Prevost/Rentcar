package com.epf.rentmanager.model;

import java.time.LocalDate;

public record Reservation(long id, long clientId, long vehicleId, LocalDate debut, LocalDate fin) {
    public Reservation(long clientId, long vehicleId, LocalDate debut, LocalDate fin) {
        this(0, clientId, vehicleId, debut, fin);
    }
}
