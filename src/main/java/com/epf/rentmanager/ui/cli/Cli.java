package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;

import java.time.LocalDate;

public class Cli {
    private ClientService clientService;
    private VehicleService vehicleService;

    public Cli() {
        this.clientService = ClientService.getInstance();
        this.vehicleService = VehicleService.getInstance();
    }

    public void createClient() {
        IOUtils.print("Saisi d'un nouveau client");
        String nom = IOUtils.readString("Nom : ", true);
        String prenom = IOUtils.readString("Prénom : ", true);
        String email = IOUtils.readEmail("Email : ");
        LocalDate naissance = IOUtils.readDate("Date de naissance (jj/mm/aaaa) : ", true);
        try {
            long idClient = clientService.create(new Client(nom, prenom, email, naissance));
            IOUtils.print(String.format("Client créé avec l'id %d", idClient));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la création du client");
        }
    }

    public void listClient() {
        IOUtils.print("Liste des clients");
        try {
            clientService.findAll().forEach(client -> IOUtils.print(client.toString()));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des clients");
        }
    }

    public void createVehicle() {
        IOUtils.print("Saisi d'un nouveau véhicule");
        String constructeur = IOUtils.readString("Constructeur : ", true);
        String model = IOUtils.readString("Modèle : ", true);
        int nbPlaces = IOUtils.readInt("Nombre de places : ");
        try {
            long idVehicle = vehicleService.create(new Vehicle(constructeur, model, nbPlaces));
            IOUtils.print(String.format("Véhicule créé avec l'id %d", idVehicle));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la création du véhicule");
        }
    }

    public void listVehicle() {
        IOUtils.print("Liste des véhicules");
        try {
            vehicleService.findAll().forEach(vehicle -> IOUtils.print(vehicle.toString()));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des véhicules");
        }
    }

    public void deleteClient() {
        IOUtils.print("Suppression d'un client");
        long id = IOUtils.readInt("Id du client à supprimer : ");
        try {
            clientService.delete(new Client(id));
            IOUtils.print("Client supprimé");
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la suppression du client");
        }
    }

    public void deleteVehicle() {
        IOUtils.print("Suppression d'un véhicule");
        long id = IOUtils.readInt("Id du véhicule à supprimer : ");
        try {
            vehicleService.delete(new Vehicle(id));
            IOUtils.print("Véhicule supprimé");
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la suppression du véhicule");
        }
    }
}
