package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.dto.ReservationClientVehicleDto;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class CommandLine {
    private ClientService clientService;
    private VehicleService vehicleService;
    private ReservationService reservationService;

    private CommandLine() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        this.clientService = context.getBean(ClientService.class);
        this.vehicleService = context.getBean(VehicleService.class);
        this.reservationService = context.getBean(ReservationService.class);
    }

    public static void main(String[] args) {
        IOUtils.print("Bienvenue dans RentManager !");
        CommandLine cli = new CommandLine();
        int choice;
        do {
            choice = IOUtils.readInt(
                    """
                    Que voulais vous faire ?
                    1. Créer un client
                    2. Lister les clients
                    3. Créer un véhicule
                    4. Lister les véhicules
                    5. Supprimer un client
                    6. Supprimer un véhicule
                    7. Reservations d'un client
                    8. Liste des réservations
                    9. Créer une réservation
                    10. Rechercher une réservation
                    11. Rerservations d'un véhicule
                    12. Quitter
                    """);
            switch (choice) {
                case 1 -> cli.createClient();
                case 2 -> cli.listClient();
                case 3 -> cli.createVehicle();
                case 4 -> cli.listVehicle();
                case 5 -> cli.deleteClient();
                case 6 -> cli.deleteVehicle();
                case 7 -> cli.listReservationsByClient();
                case 8 -> cli.listReservations();
                case 9 -> cli.createReservation();
                case 10 -> cli.findByIdReservation();
                case 11 -> cli.listReservationsByVehicle();
                case 12 -> IOUtils.print("Arrêt de RentManager");
                default -> IOUtils.print("Choix invalide");
            }
        } while (choice != 12);
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

    public void listReservationsByClient() {
        IOUtils.print("Liste des réservations d'un client");
        long id = IOUtils.readInt("Id du client : ");
        try {
            reservationService.findResaByClientId(id).forEach(reservation -> IOUtils.print(reservation.toString()));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des réservations");
        }
    }

    public void listReservations() {
        IOUtils.print("Liste des réservations");
        try {
            reservationService.findAll().forEach(reservation -> IOUtils.print(reservation.toString()));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des réservations");
        }
    }

    public void createReservation() {
        IOUtils.print("Saisi d'une nouvelle réservation");
        long idClient = IOUtils.readInt("Id du client : ");
        long idVehicle = IOUtils.readInt("Id du véhicule : ");
        LocalDate debut = IOUtils.readDate("Date de début (jj/mm/aaaa) : ", true);
        LocalDate fin = IOUtils.readDate("Date de fin (jj/mm/aaaa) : ", true);
        try {
            long idReservation = reservationService.create(new Reservation(idClient, idVehicle, debut, fin));
            IOUtils.print(String.format("Réservation créée avec l'id %d", idReservation));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la création de la réservation");
        }
    }

    public void findByIdReservation() {
        IOUtils.print("Recherche d'une réservation");
        long id = IOUtils.readInt("Id de la réservation : ");
        try {
            ReservationClientVehicleDto reservation = reservationService.findById(id);
            IOUtils.print(reservation.toString());
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération de la réservation");
        }
    }

    public void listReservationsByVehicle() {
        IOUtils.print("Liste des réservations d'un véhicule");
        long id = IOUtils.readInt("Id du véhicule : ");
        try {
            reservationService.findResaByVehicleId(id).forEach(reservation -> IOUtils.print(reservation.toString()));
        } catch (ServiceException e) {
            IOUtils.print("Erreur lors de la récupération des réservations");
        }
    }
}
