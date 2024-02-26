package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.utils.IOUtils;

public class Main {
    public static void main(String[] args) {
        IOUtils.print("Bienvenue dans RentManager !");
        Cli cli = new Cli();
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
                7. Quitter
                """);
            switch (choice) {
                case 1 -> cli.createClient();
                case 2 -> cli.listClient();
                case 3 -> cli.createVehicle();
                case 4 -> cli.listVehicle();
                case 5 -> cli.deleteClient();
                case 6 -> cli.deleteVehicle();
                case 7 -> IOUtils.print("Arrêt de RentManager");
                default -> IOUtils.print("Choix invalide");
            }
        } while (choice != 7);
    }
}
