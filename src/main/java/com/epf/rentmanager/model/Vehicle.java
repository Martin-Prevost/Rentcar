package com.epf.rentmanager.model;

public record Vehicle(long id, String constructeur, String modele, int nbPlaces) {
    public Vehicle(String constructeur, String modele, int nbPlaces) {
        this(0, constructeur, modele, nbPlaces);
    }

    public Vehicle(long id) {
        this(id, "", "", 0);
    }
}
