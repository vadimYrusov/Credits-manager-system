package com.example.bank.api.controller;

import com.example.bank.api.dto.ClientDto;
import com.example.bank.api.factories.ClientDtoFactory;
import com.example.bank.entity.Client;
import com.example.bank.exeptions.BadRequestException;
import com.example.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@Transactional
public class ClientController {

    private final ClientRepository clientRepository;

    private final ClientDtoFactory clientDtoFactory;

    @GetMapping("/client/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientRepository.getClientById(id);
    }

    @PostMapping("/add")
    public ClientDto addClient(@RequestParam String name) {

        clientRepository
                .findByName(name)
                .ifPresent(client -> {
                            throw new BadRequestException("Client " + name + " already exists");
                        });
    }

}
