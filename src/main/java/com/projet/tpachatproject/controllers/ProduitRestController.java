package com.projet.tpachatproject.controllers;


import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.services.IProduitService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@Tag(name= "Gestion des produits")
@RequestMapping("/produit")
public class ProduitRestController {


	IProduitService produitService;

	// http://localhost:8089/SpringMVC/produit/retrieve-all-produits
	@GetMapping("/retrieve-all-produits")
	@ResponseBody
	public List<Produit> getProduits() {
		List<Produit> list = produitService.retrieveAllProduits();
		return list;
	}

	// http://localhost:8089/SpringMVC/produit/retrieve-produit/8
	@GetMapping("/retrieve-produit/{produit-id}")
	@ResponseBody
	public Produit retrieveRayon(@PathVariable("produit-id") Long produitId) {
		return produitService.retrieveProduit(produitId);
	}

	/* Ajouter en produit tout en lui affectant la catégorie produit et le stock associés */
	// http://localhost:8089/SpringMVC/produit/add-produit/{idCategorieProduit}/{idStock}
	@PostMapping("/add-produit")
	@ResponseBody
	public Produit addProduit(@RequestBody Produit p) {
		Produit produit = produitService.addProduit(p);
		return produit;
	}

	// http://localhost:8089/SpringMVC/produit/remove-produit/{produit-id}
	@DeleteMapping("/remove-produit/{produit-id}")
	@ResponseBody
	public void removeProduit(@PathVariable("produit-id") Long produitId) {
		produitService.deleteProduit(produitId);
	}

	// http://localhost:8089/SpringMVC/produit/modify-produit/{idCategorieProduit}/{idStock}
	@PutMapping("/modify-produit")
	@ResponseBody
	public Produit modifyProduit(@RequestBody Produit p) {
		return produitService.updateProduit(p);
	}

	/*
	 * Si le responsable magasin souhaite modifier le stock du produit il peut
	 * le faire en l'affectant au stock en question
	 */
	// http://localhost:8089/SpringMVC/produit/assignProduitToStock/1/5
	@PutMapping(value = "/assignProduitToStock/{idProduit}/{idStock}")
	public void assignProduitToStock(@PathVariable("idProduit") Long idProduit, @PathVariable("idStock") Long idStock) {
		produitService.assignProduitToStock(idProduit, idStock);
	}

	/*
	 * Revenu Brut d'un produit (qte * prix unitaire de toutes les lignes du
	 * detailFacture du produit envoyé en paramètre )
	 */
	// http://localhost:8089/SpringMVC/produit/getRevenuBrutProduit/1/{startDate}/{endDate}
/*	@GetMapping(value = "/getRevenuBrutProduit/{idProduit}/{startDate}/{endDate}")
	public float getRevenuBrutProduit(@PathVariable("idProduit") Long idProduit,
			@PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			@PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

		return produitService.getRevenuBrutProduit(idProduit, startDate, endDate);
	}*/

}
