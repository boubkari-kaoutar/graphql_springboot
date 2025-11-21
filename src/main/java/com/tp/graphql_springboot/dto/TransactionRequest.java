package com.tp.graphql_springboot.dto;

import com.tp.graphql_springboot.entities.TypeTransaction;

public class TransactionRequest {
    private Long compteId;
    private Double montant;
    private String date;
    private TypeTransaction type;

    // Constructeurs
    public TransactionRequest() {
    }

    public TransactionRequest(Long compteId, Double montant, String date, TypeTransaction type) {
        this.compteId = compteId;
        this.montant = montant;
        this.date = date;
        this.type = type;
    }

    // Getters et Setters
    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TypeTransaction getType() {
        return type;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }
}