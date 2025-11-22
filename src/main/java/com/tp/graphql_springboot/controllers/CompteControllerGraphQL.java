package com.tp.graphql_springboot.controllers;

import com.tp.graphql_springboot.dto.CompteRequest;
import com.tp.graphql_springboot.dto.TransactionRequest;
import com.tp.graphql_springboot.entities.Compte;
import com.tp.graphql_springboot.entities.Transaction;
import com.tp.graphql_springboot.entities.TypeTransaction;
import com.tp.graphql_springboot.repositories.CompteRepository;
import com.tp.graphql_springboot.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class CompteControllerGraphQL {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @QueryMapping
    public List<Compte> allComptes(){
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument Long id){
        Compte compte = compteRepository.findById(id).orElse(null);
        if(compte == null) throw new RuntimeException(String.format("Compte %s not found", id));
        else return compte;
    }

    @MutationMapping
    public Compte saveCompte(@Argument CompteRequest compte) {
        Compte newCompte = new Compte();
        newCompte.setSolde(compte.getSolde());
        newCompte.setType(compte.getType());
        newCompte.setDateCreation(new Date()); // Date automatique

        return compteRepository.save(newCompte);
    }

    @MutationMapping
    public boolean deleteCompte(@Argument Long id) {
        if (compteRepository.existsById(id)) {
            compteRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Compte not found");
    }

    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = compteRepository.count();
        double sum = compteRepository.sumSoldes();
        double average = count > 0 ? sum / count : 0;

        return Map.of(
                "count", count,
                "sum", sum,
                "average", average
        );
    }

    // ===== MÉTHODES POUR LES TRANSACTIONS =====

    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        Transaction newTransaction = new Transaction();
        newTransaction.setMontant(transactionRequest.getMontant());
        newTransaction.setType(transactionRequest.getType());
        newTransaction.setDate(new Date()); // Date automatique
        newTransaction.setCompte(compte);

        // Mise à jour du solde du compte
        if (transactionRequest.getType() == TypeTransaction.DEPOT) {
            compte.setSolde(compte.getSolde() + transactionRequest.getMontant());
        } else {
            if (compte.getSolde() < transactionRequest.getMontant()) {
                throw new RuntimeException("Solde insuffisant pour effectuer ce retrait");
            }
            compte.setSolde(compte.getSolde() - transactionRequest.getMontant());
        }
        compteRepository.save(compte);

        return transactionRepository.save(newTransaction);
    }

    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        Double sumDepots = transactionRepository.sumByType(TypeTransaction.DEPOT);
        Double sumRetraits = transactionRepository.sumByType(TypeTransaction.RETRAIT);

        return Map.of(
                "count", count,
                "sumDepots", sumDepots != null ? sumDepots : 0.0,
                "sumRetraits", sumRetraits != null ? sumRetraits : 0.0
        );
    }
}