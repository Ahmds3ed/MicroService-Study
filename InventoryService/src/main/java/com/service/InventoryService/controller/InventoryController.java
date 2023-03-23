package com.service.InventoryService.controller;

import com.service.InventoryService.dto.InventoryResponse;
import com.service.InventoryService.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isOnStock(@RequestParam(name = "skuCode") List<String> skuCodes) throws InterruptedException {
        return inventoryService.isAvailabeOnStock(skuCodes);
    }
}
