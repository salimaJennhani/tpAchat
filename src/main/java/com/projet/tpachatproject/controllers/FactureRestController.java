package com.projet.tpachatproject.controllers;


import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.services.IFactureService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/facture")
@Tag(name = "Gestion des Facturesssss" )
@CrossOrigin("*")
@AllArgsConstructor
public class FactureRestController {


    IFactureService factureService;

    // http://localhost:8089/SpringMVC/facture/retrieve-all-factures
    @GetMapping("/retrieve-all-factures")

    public List<Facture> getFactures() {
        return factureService.retrieveAllFactures();
    }

    // http://localhost:8089/SpringMVC/facture/retrieve-facture/8
    @GetMapping("/retrieve-facture/{facture-id}")

    public Facture retrieveFacture(@PathVariable("facture-id") Long factureId) {
        return factureService.retrieveFacture(factureId);
    }

    // http://localhost:8089/SpringMVC/facture/add-facture/{fournisseur-id}
    @PostMapping("/add-facture")

    public Facture addFacture(@RequestBody Facture f) {
        return factureService.addFacture(f);
    }


    // http://localhost:8089/SpringMVC/facture/cancel-facture/{facture-id}
    @PutMapping("/cancel-facture/{facture-id}")

    public void cancelFacture(@PathVariable("facture-id") Long factureId) {
        factureService.cancelFacture(factureId);
    }

    // http://localhost:8089/SpringMVC/facture/getFactureByFournisseur/{fournisseur-id}
    @GetMapping("/getFactureByFournisseur/{fournisseur-id}")

    public List<Facture> getFactureByFournisseur(@PathVariable("fournisseur-id") Long fournisseurId) {
        return factureService.getFacturesByFournisseur(fournisseurId);
    }

    // http://localhost:8089/SpringMVC/facture/assignOperateurToFacture/1/1
    @PutMapping(value = "/assignOperateurToFacture/{idOperateur}/{idFacture}")
    public void assignOperateurToFacture(@PathVariable("idOperateur") Long idOperateur, @PathVariable("idFacture") Long idFacture) {
        factureService.assignOperateurToFacture(idOperateur, idFacture);
    }

    // http://localhost:8089/SpringMVC/facture/pourcentageRecouvrement/{startDate}/{endDate}
    @GetMapping(value = "/pourcentageRecouvrement/{startDate}/{endDate}")
    public float pourcentageRecouvrement(
            @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            return factureService.pourcentageRecouvrement(startDate, endDate);
        } catch (Exception e) {
            return 0;
        }
    }

}
