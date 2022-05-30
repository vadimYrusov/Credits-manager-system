package com.example.bank.api.controller;

import com.example.bank.entity.Client;
import com.example.bank.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/client/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }
}
