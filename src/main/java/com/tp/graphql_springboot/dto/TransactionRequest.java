package com.tp.graphql_springboot.dto;

import com.tp.graphql_springboot.entities.TypeTransaction;

public class TransactionRequest {
    private Long compteId;
    private Double montant;
    private TypeTransaction type;

    // Constructeurs
    public TransactionRequest() {
    }

    public TransactionRequest(Long compteId, Double montant, TypeTransaction type) {
        this.compteId = compteId;
        this.montant = montant;
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

    public TypeTransaction getType() {
        return type;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }
}