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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        // Conversion de String vers Date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(compte.getDateCreation());
            newCompte.setDateCreation(date);
        } catch (ParseException e) {
            throw new RuntimeException("Format de date invalide. Utilisez le format: yyyy-MM-dd");
        }

        return compteRepository.save(newCompte);
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
    public Transaction addTransaction(@Argument TransactionRequest transaction) {
        Compte compte = compteRepository.findById(transaction.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        Transaction newTransaction = new Transaction();
        newTransaction.setMontant(transaction.getMontant());
        newTransaction.setType(transaction.getType());
        newTransaction.setCompte(compte);

        // Conversion de String vers Date (accepte / et -)
        try {
            String dateString = transaction.getDate().replace("/", "-");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            newTransaction.setDate(date);
        } catch (ParseException e) {
            throw new RuntimeException("Format de date invalide. Utilisez: yyyy-MM-dd ou yyyy/MM/dd");
        }

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