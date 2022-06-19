package com.example.bank.api.controller;

import com.example.bank.api.dto.AnswerDto;
import com.example.bank.api.dto.ClientDto;
import com.example.bank.api.dto.CreditDto;
import com.example.bank.api.exeptions.BadRequestException;
import com.example.bank.api.exeptions.NotFoundException;
import com.example.bank.api.factories.ClientDtoFactory;
import com.example.bank.api.factories.CreditDtoFactory;
import com.example.bank.entity.Client;
import com.example.bank.entity.Credit;
import com.example.bank.repository.ClientRepository;
import com.example.bank.repository.CreditRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operations with clients")
public class ClientController {

    private final ClientRepository clientRepository;

    private final CreditRepository creditRepository;

    private final ClientDtoFactory clientDtoFactory;

    private final CreditDtoFactory creditDtoFactory;

    @Operation(summary = "All clients", description = "Get all clients")
    @GetMapping("/all")
    public List<ClientDto> getAllClients() {

        List<Client> clients = clientRepository.findAll();

        return clients.isEmpty() ?
                Collections.emptyList() :
                clients.stream().map(clientDtoFactory::makeClientDto).collect(Collectors.toList());
    }

    @Operation(summary = "Get client", description = "Get client by id")
    @GetMapping("/client/{id}")
    public ClientDto getClient(@PathVariable Long id) {

        Client client = getClientOrThrowException(id);

        return clientDtoFactory.makeClientDto(client);
    }

    @Operation(summary = "Add client", description = "Create client")
    @PostMapping("/add")
    public ClientDto addClient(@RequestBody Client client) {

        if (client.getName().trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty");
        } else if (client.getSurname().trim().isEmpty()) {
            throw new BadRequestException("Surname can't be empty");
        } else if (client.getAge() == 0) {
            throw new BadRequestException("Age can't be 0");
        } else if (client.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email can't be empty");
        }

        Client client1 = clientRepository.saveAndFlush(
                Client.builder()
                        .name(client.getName())
                        .surname(client.getSurname())
                        .age(client.getAge())
                        .email(client.getEmail())
                        .build()
        );

        return clientDtoFactory.makeClientDto(client1);
    }

    @Operation(summary = "Edit client", description = "Change client parameters")
    @PatchMapping("/edit/{client_id}")
    public ClientDto editClient(@PathVariable("client_id") Long id,
                                @RequestBody Client client) {



        Client client1 = getClientOrThrowException(id);

        client1.setName(client.getName());
        client1.setSurname(client.getSurname());
        client1.setAge(client.getAge());
        client1.setEmail(client.getEmail());

        client1 = clientRepository.saveAndFlush(client1);

        return clientDtoFactory.makeClientDto(client1);
    }

    @Operation(summary = "Delete client", description = "Delete client")
    @DeleteMapping("/delete/{client_id}")
    public AnswerDto deleteClient(@PathVariable("client_id") Long id) {

        Client client = getClientOrThrowException(id);

        clientRepository.deleteById(id);

        return AnswerDto.makeDefault(true);
    }

    @Operation(summary = "Credit line", description = "Get client credits")
    @GetMapping("/{client_id}/credits")
    public List<CreditDto> getCreditLine(@PathVariable("client_id") Long id) {

        Client client = getClientOrThrowException(id);

        return client
                .getCredits()
                .stream()
                .map(creditDtoFactory::makeCreditDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Add credit", description = "Add credit to client credit line")
    @PutMapping("/{client_id}/credit/{credit_id}")
    public ClientDto addCreditToClient(@PathVariable("client_id") Long client_id,
                                       @PathVariable("credit_id") Long credit_id
    ) {
        Client client = getClientOrThrowException(client_id);

        Credit credit = creditRepository
                .findById(credit_id)
                .orElseThrow(() ->
                        new NotFoundException("Credit with id " + credit_id + " doesn't exist"));

        client.getCredits().add(credit);

        client = clientRepository.saveAndFlush(client);

        return clientDtoFactory.makeClientDto(client);
    }

    private Client getClientOrThrowException(Long id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Client with id " + id + " doesn't exist")
                );
    }
}
