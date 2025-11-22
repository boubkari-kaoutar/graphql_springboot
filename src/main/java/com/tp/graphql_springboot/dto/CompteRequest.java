package com.tp.graphql_springboot.dto;

import com.tp.graphql_springboot.entities.TypeCompte;

public class CompteRequest {
    private Double solde;
    private TypeCompte type;

    // Constructeurs
    public CompteRequest() {
    }

    public CompteRequest(Double solde, TypeCompte type) {
        this.solde = solde;
        this.type = type;
    }

    // Getters et Setters
    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public TypeCompte getType() {
        return type;
    }

    public void setType(TypeCompte type) {
        this.type = type;
    }
}