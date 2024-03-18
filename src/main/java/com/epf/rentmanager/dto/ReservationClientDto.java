package com.epf.rentmanager.dto;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;

import java.time.LocalDate;

public record ReservationClientDto(long id, Vehicle vehicle, Client client, LocalDate debut, LocalDate fin) {}

