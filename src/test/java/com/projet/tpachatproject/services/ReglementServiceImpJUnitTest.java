package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.entities.Reglement;
import org.junit.jupiter.api.*;
import org.springframework.core.annotation.Order;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReglementServiceImpJUnitTest {
    private Facture facture;
    private Reglement reglement;

    @BeforeEach
    public void setUp() {
        facture = new Facture();
        facture.setMontantFacture(5000.0f); // Montant de la facture = 5000 DT

        reglement = new Reglement();
        reglement.setMontantPaye(0.0f); // Aucun paiement initial
        reglement.setMontantRestant(facture.getMontantFacture()); // Montant restant = montant total de la facture
        facture.setDateDerniereModificationFacture(new Date()); // Date initiale de création
    }

    @Test
    @Order(1)
    public void testPaiementComplet() {
        // Simuler un paiement complet
        reglement.setMontantPaye(facture.getMontantFacture()); // Paiement complet
        reglement.setMontantRestant(0.0f); // Aucun montant restant
        reglement.setPayee(true); // Marquer la facture comme payée

        // Assertions
        assertEquals(0.0f, reglement.getMontantRestant(), 0.001, "Il ne doit pas rester de montant à payer.");
        assertTrue(reglement.isPayee(), "La facture doit être marquée comme payée.");
    }

    @Test
    @Order(2)
    public void testNouvelleAvanceAvant30Jours() {
        // Paiement initial partiel
        reglement.setMontantPaye(2000.0f); // Paiement initial de 2000 DT
        reglement.setMontantRestant(facture.getMontantFacture() - reglement.getMontantPaye()); // 5000 - 2000 = 3000
        facture.setDateDerniereModificationFacture(new Date()); // Date initiale de modification

        // Nouvelle avance après 25 jours (avant 30 jours)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 25); // Simuler un paiement après 25 jours
        Date nouvelleAvanceDate = cal.getTime();

        // Simuler une nouvelle avance avant 30 jours
        float nouvelleAvance = 1000.0f; // Nouvelle avance de 1000 DT
        reglement.setMontantPaye(reglement.getMontantPaye() + nouvelleAvance); // 2000 + 1000 = 3000
        reglement.setMontantRestant(facture.getMontantFacture() - reglement.getMontantPaye()); // 5000 - 3000 = 2000
        facture.setDateDerniereModificationFacture(new Date()); // Mettre à jour la date de modification

        // Assertions
        assertEquals(2000.0f, reglement.getMontantRestant(), 0.001, "Le montant restant doit être de 2000 DT après la nouvelle avance."); // Corrected
        long difference = Math.abs(new Date().getTime() - facture.getDateDerniereModificationFacture().getTime());
        assertTrue(difference < 1000, "La date de modification doit être mise à jour.");
    }

    @Test
    @Order(3)
    public void testPenaliteApres30JoursSansPaiement() {
        // Paiement initial non effectué
        reglement.setMontantPaye(0.0f); // Aucun paiement initial
        reglement.setMontantRestant(facture.getMontantFacture()); // Montant restant = montant total de la facture

        // Simuler un dépassement de 60 jours sans paiement
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 60); // Simuler 60 jours plus tard
        Date dateDepassement = cal.getTime();

        // Vérifiez si la date actuelle est après la date de dépassement
        if (new Date().after(dateDepassement)) {
            float penalite = (facture.getMontantFacture() * 5) / 100; // 5% de pénalité

            // Appliquer la pénalité
            reglement.setMontantRestant(reglement.getMontantRestant() + penalite); // Ajouter la pénalité au montant restant

            // Mettre à jour le montant de la facture
            facture.setMontantFacture(facture.getMontantFacture() + penalite); // Actualiser le montant de la facture

            // Mettre à jour la date de modification de la facture
            facture.setDateDerniereModificationFacture(new Date());
        }

        // Assertions
        float montantRestantAvecPenalite = facture.getMontantFacture(); // Montant total de la facture après pénalité
        assertEquals(montantRestantAvecPenalite, reglement.getMontantRestant(), 0.001, "Le montant restant doit être de " + montantRestantAvecPenalite + " DT après la pénalité.");

        // Vérification que la date de modification est mise à jour
        long difference = Math.abs(new Date().getTime() - facture.getDateDerniereModificationFacture().getTime());
        assertTrue(difference < 1000, "La date de modification doit être mise à jour après l'application de la pénalité.");
    }


}
