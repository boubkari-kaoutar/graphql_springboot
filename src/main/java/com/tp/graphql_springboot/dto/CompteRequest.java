package com.tp.graphql_springboot.dto;

import com.tp.graphql_springboot.entities.TypeCompte;

public class CompteRequest {
    private Double solde;
    private String dateCreation;
    private TypeCompte type;

    // Constructeurs
    public CompteRequest() {
    }

    public CompteRequest(Double solde, String dateCreation, TypeCompte type) {
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
    }

    // Getters et Setters
    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeCompte getType() {
        return type;
    }

    public void setType(TypeCompte type) {
        this.type = type;
    }
}