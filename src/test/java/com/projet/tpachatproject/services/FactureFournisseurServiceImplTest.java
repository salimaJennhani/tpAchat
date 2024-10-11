package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.*;
import com.projet.tpachatproject.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FactureFournisseurServiceImplTest {


    @Mock
    FactureRepository factureRepository;
    @Mock
    FournisseurRepository fournisseurRepository;
    @Mock
    ProduitRepository produitRepository;
    @InjectMocks
    FactureFournisseurService factureFournisseurService;

    @Mock
    DetailFactureRepository detailFactureRepository;
    @Test
    public void testAddFactureWithFournisseur_Ordinaire() {
        // Arrange
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        fournisseur.setCategorieFournisseur(CategorieFournisseur.ORDINAIRE);

        Facture facture = new Facture();
        Set<DetailFacture> detailsFacture = new HashSet<>();
        DetailFacture detail = new DetailFacture();
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        produit.setPrix(100);
        detail.setProduit(produit);
        detail.setQteCommandee(2);
        detail.setPourcentageRemise(5);  // 5% Remise
        detailsFacture.add(detail);
        facture.setDetailsFacture(detailsFacture);

        // Mock repository responses
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(factureRepository.save(ArgumentMatchers.<Facture>any())).thenReturn(facture);
        when(detailFactureRepository.save(ArgumentMatchers.<DetailFacture>any())).thenReturn(detail);

        // Act
        Facture savedFacture = factureFournisseurService.addFactureWithFournisseur(facture, 1L);

        // Assert
        assertNotNull(savedFacture);

        // Calculate expected and actual discounts
        double expectedDiscount = produit.getPrix() * (detail.getPourcentageRemise() / 100.0);
        double expectedMontant = produit.getPrix() - expectedDiscount;

        // Print values for debugging
        System.out.println("Expected Montant: " + expectedMontant);
        System.out.println("Actual Montant: " + savedFacture.getMontantFacture());
        System.out.println("Expected Discount: " + expectedDiscount);
        double actualDiscount = produit.getPrix() - savedFacture.getMontantFacture();
        System.out.println("Actual Discount: " + actualDiscount);

        assertEquals(190.0, savedFacture.getMontantFacture(), 0.0);
        verify(factureRepository, times(1)).save(facture);
        verify(detailFactureRepository, times(1)).save(detail);

    }

    @Test
    public void testAddFactureWithFournisseur_Conventionne() {
        // Arrange
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(2L);
        fournisseur.setCategorieFournisseur(CategorieFournisseur.CONVENTIONNE);

        Facture facture = new Facture();
        Set<DetailFacture> detailsFacture = new HashSet<>();
        DetailFacture detail = new DetailFacture();
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        produit.setPrix(200);
        detail.setProduit(produit);
        detail.setQteCommandee(1);
        detail.setPourcentageRemise(10);  // 10% Remise
        detailsFacture.add(detail);
        facture.setDetailsFacture(detailsFacture);

        // Mock repository responses
        when(fournisseurRepository.findById(2L)).thenReturn(Optional.of(fournisseur));
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(factureRepository.save(ArgumentMatchers.<Facture>any())).thenReturn(facture);
        when(detailFactureRepository.save(ArgumentMatchers.<DetailFacture>any())).thenReturn(detail);

        // Act
        Facture savedFacture = factureFournisseurService.addFactureWithFournisseur(facture, 2L);

        // Assert
        assertNotNull(savedFacture);

        // Calculate expected and actual discounts
        double expectedDiscount = produit.getPrix() * (detail.getPourcentageRemise() / 100.0);
        double expectedMontant = produit.getPrix() - expectedDiscount;

        // Print values for debugging
        System.out.println("Expected Montant: " + expectedMontant);
        System.out.println("Actual Montant: " + savedFacture.getMontantFacture());
        System.out.println("Expected Discount: " + expectedDiscount);
        double actualDiscount = produit.getPrix() - savedFacture.getMontantFacture();
        System.out.println("Actual Discount: " + actualDiscount);

        assertEquals(180.0, savedFacture.getMontantFacture(), 0.0);
        verify(factureRepository, times(1)).save(facture);
        verify(detailFactureRepository, times(1)).save(detail);

    }

    @Test
    public void testAddFactureWithFournisseur_FirstTime() {
        // Arrange
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(3L);
        fournisseur.setCategorieFournisseur(CategorieFournisseur.FIRSTTIME);

        Facture facture = new Facture();
        Set<DetailFacture> detailsFacture = new HashSet<>();
        DetailFacture detail = new DetailFacture();
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        produit.setPrix(150);
        detail.setProduit(produit);
        detail.setQteCommandee(1);
        detail.setPourcentageRemise(5);  // 5% Remise
        detailsFacture.add(detail);
        facture.setDetailsFacture(detailsFacture);

        // Mock repository responses
        when(fournisseurRepository.findById(3L)).thenReturn(Optional.of(fournisseur));
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(factureRepository.save(ArgumentMatchers.<Facture>any())).thenReturn(facture);
        when(detailFactureRepository.save(ArgumentMatchers.<DetailFacture>any())).thenReturn(detail);


        // Act
        Facture savedFacture = factureFournisseurService.addFactureWithFournisseur(facture, 3L);

        // Assert
        assertNotNull(savedFacture);

        // Calculate expected and actual discounts
        double expectedDiscount = produit.getPrix() * (detail.getPourcentageRemise() / 100.0);
        double expectedMontant = produit.getPrix() - expectedDiscount;

        // Print values for debugging
        System.out.println("Expected Montant: " + expectedMontant);
        System.out.println("Actual Montant: " + savedFacture.getMontantFacture());
        System.out.println("Expected Discount: " + expectedDiscount);
        double actualDiscount = produit.getPrix() - savedFacture.getMontantFacture();
        System.out.println("Actual Discount: " + actualDiscount);

        assertEquals(142.5, savedFacture.getMontantFacture(), 0.0);
        verify(factureRepository, times(1)).save(facture);
        verify(detailFactureRepository, times(1)).save(detail);

    }

}