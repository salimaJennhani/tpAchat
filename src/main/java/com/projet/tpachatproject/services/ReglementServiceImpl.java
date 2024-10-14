package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.entities.Reglement;
import com.projet.tpachatproject.repositories.FactureRepository;
import com.projet.tpachatproject.repositories.ReglementRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class ReglementServiceImpl implements IReglementService {


	FactureRepository factureRepository;

	ReglementRepository reglementRepository;


	@Override
	public List<Reglement> retrieveAllReglements() {
		return (List<Reglement>) reglementRepository.findAll();
	}

	@Override
	public Reglement addReglement(Reglement r) {
        reglementRepository.save(r);
		return r;
	}

	@Override
	public Reglement retrieveReglement(Long id) {
		Reglement reglement = reglementRepository.findById(id).orElse(null);
		
		return reglement;
	}

	@Override
	public List<Reglement> retrieveReglementByFacture(Long idFacture) {
		List<Reglement> reglements= reglementRepository.retrieveReglementByFacture(idFacture);
		return reglements;
		
//		ou bien(Sans JPQL)
//		Facture f= factureRepository.findById(idFacture).get();
//		return (List<Reglement>) f.getReglements();
	}

	@Override
	public float getChiffreAffaireEntreDeuxDate(Date startDate, Date endDate) {
		return reglementRepository.getChiffreAffaireEntreDeuxDate( startDate, endDate);
	}

	public void enregistrerAvance(Reglement reglement, float montantPaye, Facture facture) {
		// Vérifier si le montant payé est inférieur au montant de la facture
		if (montantPaye < facture.getMontantFacture()) {
			// Mettre à jour le montant payé et le montant restant
			reglement.setMontantPaye(montantPaye);
			float montantRestant = facture.getMontantFacture() - montantPaye;
			reglement.setMontantRestant(montantRestant);
			reglement.setPayee(false); // Marquer le règlement comme non payé

			// Mettre à jour la date de la dernière modification de la facture
			facture.setDateDerniereModificationFacture(new Date());
		} else {
			// Si le montant payé est égal au montant de la facture, marquer le règlement comme payé
			reglement.setMontantPaye(facture.getMontantFacture());
			reglement.setMontantRestant(0.0f);
			reglement.setPayee(true); // Marquer le règlement comme payé
		}
	}

	public void appliquerPenalite(Reglement reglement, Facture facture) {
		// Vérifier si le paiement n'a pas été effectué et que la date de dernière modification est dépassée de 30 jours
		long differenceEnJours = (new Date().getTime() - facture.getDateDerniereModificationFacture().getTime()) / (1000 * 60 * 60 * 24);
		if (reglement.getMontantPaye() == 0.0f && differenceEnJours > 30) {
			float penalite = (facture.getMontantFacture() * 5) / 100; // Calculer la pénalité (5%)
			reglement.setMontantRestant(reglement.getMontantRestant() + penalite); // Ajouter la pénalité au montant restant
			facture.setMontantFacture(facture.getMontantFacture() + penalite); // Mettre à jour le montant de la facture
			facture.setDateDerniereModificationFacture(new Date()); // Mettre à jour la date de modification
		}
	}
}
