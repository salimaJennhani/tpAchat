package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.entities.Stock;
import com.projet.tpachatproject.repositories.ProduitRepository;
import com.projet.tpachatproject.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduitServiceImplJUnitTest {

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAndUpdateProduitStock_ProduitNotFound() {
        when(produitRepository.findById(anyLong())).thenReturn(Optional.empty());

        String result = produitService.checkAndUpdateProduitStock(1L, 10);

        System.out.println("Test: Produit Not Found");
        System.out.println("Expected: Produit not found");
        System.out.println("Actual: " + result);
        System.out.println("Test result: " + (result.equals("Produit not found") ? "PASSED" : "FAILED"));
        System.out.println();

        assertEquals("Produit not found", result);
        verify(produitRepository).findById(1L);
        verifyNoInteractions(stockRepository);
    }

    @Test
    void checkAndUpdateProduitStock_NoAssociatedStock() {
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        produit.setStock(null);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        String result = produitService.checkAndUpdateProduitStock(1L, 10);

        System.out.println("Test: No Associated Stock");
        System.out.println("Expected: Produit has no associated stock");
        System.out.println("Actual: " + result);
        System.out.println("Test result: " + (result.equals("Produit has no associated stock") ? "PASSED" : "FAILED"));
        System.out.println();

        assertEquals("Produit has no associated stock", result);
        verify(produitRepository).findById(1L);
        verifyNoInteractions(stockRepository);
    }

    @Test
    void checkAndUpdateProduitStock_NegativeStock() {
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        Stock stock = new Stock();
        stock.setQte(5);
        produit.setStock(stock);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        String result = produitService.checkAndUpdateProduitStock(1L, -10);

        System.out.println("Test: Negative Stock");
        System.out.println("Expected: Cannot remove more items than available in stock");
        System.out.println("Actual: " + result);
        System.out.println("Test result: " + (result.equals("Cannot remove more items than available in stock") ? "PASSED" : "FAILED"));
        System.out.println();

        assertEquals("Cannot remove more items than available in stock", result);
        verify(produitRepository).findById(1L);
        verifyNoInteractions(stockRepository);
    }

    @Test
    void checkAndUpdateProduitStock_LowStock() {
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        Stock stock = new Stock();
        stock.setQte(10);
        stock.setQteMin(15);
        produit.setStock(stock);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        String result = produitService.checkAndUpdateProduitStock(1L, 2);

        System.out.println("Test: Low Stock");
        System.out.println("Expected: Stock level low");
        System.out.println("Actual: " + result);
        System.out.println("Initial stock quantity: 10");
        System.out.println("Final stock quantity: " + stock.getQte());
        System.out.println("Test result: " + (result.equals("Stock level low") && stock.getQte() == 12 ? "PASSED" : "FAILED"));
        System.out.println();

        assertEquals("Stock level low", result);
        assertEquals(12, stock.getQte());
        verify(produitRepository).findById(1L);
        verify(stockRepository).save(stock);
    }

    @Test
    void checkAndUpdateProduitStock_AdequateStock() {
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        Stock stock = new Stock();
        stock.setQte(15);
        stock.setQteMin(10);
        produit.setStock(stock);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        String result = produitService.checkAndUpdateProduitStock(1L, 5);

        System.out.println("Test: Adequate Stock");
        System.out.println("Expected: Stock level adequate");
        System.out.println("Actual: " + result);
        System.out.println("Initial stock quantity: 15");
        System.out.println("Final stock quantity: " + stock.getQte());
        System.out.println("Test result: " + (result.equals("Stock level adequate") && stock.getQte() == 20 ? "PASSED" : "FAILED"));
        System.out.println();

        assertEquals("Stock level adequate", result);
        assertEquals(20, stock.getQte());
        verify(produitRepository).findById(1L);
        verify(stockRepository).save(stock);
    }

    @Test
    void checkAndUpdateProduitStock_HighStock() {
        Produit produit = new Produit();
        produit.setIdProduit(1L);
        Stock stock = new Stock();
        stock.setQte(25);
        stock.setQteMin(10);
        produit.setStock(stock);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        String result = produitService.checkAndUpdateProduitStock(1L, 5);

        System.out.println("Test: High Stock");
        System.out.println("Expected: Stock level high");
        System.out.println("Actual: " + result);
        System.out.println("Initial stock quantity: 25");
        System.out.println("Final stock quantity: " + stock.getQte());
        System.out.println("Test result: " + (result.equals("Stock level high") && stock.getQte() == 30 ? "PASSED" : "FAILED"));
        System.out.println();

        assertEquals("Stock level high", result);
        assertEquals(30, stock.getQte());
        verify(produitRepository).findById(1L);
        verify(stockRepository).save(stock);
    }
}