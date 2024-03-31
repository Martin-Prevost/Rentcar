package com.epf.rentmanager.dto;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;

import java.time.LocalDate;

public record ReservationClientVehicleDto(long id, Vehicle vehicle, Client client, LocalDate debut, LocalDate fin) {}

