package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.entities.Reglement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReglementServiceMockTest {
    @InjectMocks
    private ReglementServiceImpl reglementService;

    @Mock
    private Facture facture;

    @Mock
    private Reglement reglement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks
    }

    @Test
    public void testNouvelleAvanceAvant30Jours() {
        // Initialiser les mocks
        when(facture.getMontantFacture()).thenReturn(5000.0f);
        when(reglement.getMontantRestant()).thenReturn(5000.0f);
        when(reglement.getMontantPaye()).thenReturn(0.0f);

        // Simuler un paiement
        float montantPaye = 2000.0f;
        reglementService.enregistrerAvance(reglement, montantPaye, facture);

        // Vérifications
        verify(reglement).setMontantPaye(montantPaye); // Vérifie que le montant payé a été mis à jour
        verify(reglement).setMontantRestant(3000.0f); // Vérifie que le montant restant est correct (5000 - 2000)
        verify(facture).setDateDerniereModificationFacture(any(Date.class)); // Vérifie que la date de modification a été mise à jour
    }

    @Test
    public void testPenaliteApres30JoursSansPaiement() {
        // Initialiser les mocks
        when(facture.getMontantFacture()).thenReturn(5000.0f);
        when(reglement.getMontantPaye()).thenReturn(0.0f); // Aucun paiement initial
        when(reglement.getMontantRestant()).thenReturn(5000.0f); // Montant restant initial

        // Simuler la date de dernière modification
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -60); // Simuler que la date de modification est plus ancienne
        Date dateModification = cal.getTime();
        when(facture.getDateDerniereModificationFacture()).thenReturn(dateModification);

        // Simuler le scénario où il y a une pénalité
        reglementService.appliquerPenalite(reglement, facture);

        // Calculer la pénalité
        float penalite = (facture.getMontantFacture() * 5) / 100; // 5% de pénalité
        float montantRestantAttendu = 5000.0f + penalite; // Montant restant après pénalité

        // Vérifications
        verify(reglement).setMontantRestant(montantRestantAttendu); // Montant restant après pénalité
        verify(facture).setMontantFacture(montantRestantAttendu); // Montant de la facture mis à jour
        verify(facture).setDateDerniereModificationFacture(any(Date.class)); // Vérifie que la date de modification a été mise à jour
    }

}
