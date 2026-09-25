package com.logonedigital.MBOAcare.controller;

import com.logonedigital.MBOAcare.dto.StockReqdto;
import com.logonedigital.MBOAcare.dto.StockResdto;
import com.logonedigital.MBOAcare.service.stock.StockService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/stock")

    public class StockController {
        private final StockService stockService;

        public StockController(StockService stockService) {
            this.stockService = stockService;
        }

        @PostMapping(path = "/create")
        public ResponseEntity<String> createStock(@Valid @RequestBody StockReqdto stockReqdto) {
            this.stockService.addStock(stockReqdto);
            return ResponseEntity.status(201).body("Stock cree avec succes !");
        }

        @GetMapping(path = "/get_by_id/{idStock}")
        public ResponseEntity<StockResdto> getStockById(@PathVariable String idStock) {
            return ResponseEntity.status(200)
                    .body(this.stockService.getStockById(idStock));
        }

        @GetMapping(path = "/get_all")
        public ResponseEntity<List<StockResdto>> getStock() {
            return ResponseEntity.status(200).body(this.stockService.getAllStock());
        }

        @PutMapping("/update_by_id/{idStock}")
        public ResponseEntity<String> updateStock(@PathVariable String idStock, @Valid @RequestBody StockReqdto stockReqdto) {

            this.stockService.updateStock(idStock, stockReqdto);
            return ResponseEntity.status(202).body("Stock modifie avec succes !");
        }



        @DeleteMapping(path = "delete_by_id/{idStock}")
        public ResponseEntity<String> deleteStock(@PathVariable String idStock) {
            this.stockService.deleteStock(idStock);
            return ResponseEntity.status(202).body("Stock supprime avec succces!");
        }

        @GetMapping("/pagination")
        public Page<StockResdto> getPaginated(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "nom") String sortBy
        ) {
            return stockService.getPaginated(page, size, sortBy);
        }
    }

