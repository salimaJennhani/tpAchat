package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.*;
import com.projet.tpachatproject.repositories.DetailFactureRepository;
import com.projet.tpachatproject.repositories.FactureRepository;
import com.projet.tpachatproject.repositories.FournisseurRepository;
import com.projet.tpachatproject.repositories.ProduitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class FactureFournisseurService {

    @Autowired
    FactureRepository factureRepository;

    @Autowired
    FournisseurRepository fournisseurRepository;

    @Autowired
    DetailFactureRepository detailFactureRepository;

    @Autowired
    ProduitRepository produitRepository;

    @Autowired
    ReglementServiceImpl reglementService;

    /**
     * Combine Facture and Fournisseur by calculating amounts and applying rules
     */
    public Facture addFactureWithFournisseur(Facture facture, Long fournisseurId) {
        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElse(null);

        if (fournisseur == null) {
            throw new IllegalArgumentException("Fournisseur does not exist");
        }

        // Apply the discount rule based on Fournisseur's CategorieFournisseur
        switch (fournisseur.getCategorieFournisseur()) {
            case CONVENTIONNE:
                log.info("Applying discount for CONVENTIONNE Fournisseur");
                applyFournisseurDiscount(facture, 10);  // 10% discount for CONVENTIONNE
                break;
            case FIRSTTIME:
                log.info("Applying discount for FIRSTTIME Fournisseur");
                applyFournisseurDiscount(facture, 5);   // 5% discount for FIRSTTIME
                break;
            case ORDINAIRE:
            default:
                log.info("No discount for ORDINAIRE Fournisseur");
                break;
        }

        // Add details of Facture and calculate amounts
        Set<DetailFacture> detailsFacture = facture.getDetailsFacture();
        float montantTotal = 0;
        float montantRemise = 0;

        for (DetailFacture detail : detailsFacture) {
            Produit produit = produitRepository.findById(detail.getProduit().getIdProduit()).orElse(null);
            if (produit != null) {
                float prixTotalDetail = detail.getQteCommandee() * produit.getPrix();
                float montantRemiseDetail = (prixTotalDetail * detail.getPourcentageRemise()) / 100;
                float prixTotalRemise = prixTotalDetail - montantRemiseDetail;

                detail.setMontantRemise(montantRemiseDetail);
                detail.setPrixTotalDetail(prixTotalRemise);

                montantTotal += prixTotalRemise;
                montantRemise += montantRemiseDetail;

                detailFactureRepository.save(detail);
            }
        }

        facture.setMontantFacture(montantTotal);
        facture.setMontantRemise(montantRemise);

        // Save facture with fournisseur association
        facture.setFournisseur(fournisseur);
        return factureRepository.save(facture);
    }

    private void applyFournisseurDiscount(Facture facture, float discountPercent) {
        float montantFacture = facture.getMontantFacture();
        float discount = (montantFacture * discountPercent) / 100;
        facture.setMontantFacture(montantFacture - discount);
        facture.setMontantRemise(facture.getMontantRemise() + discount);
    }
}