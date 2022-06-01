package com.example.bank.api.controller;

import com.example.bank.api.dto.AnswerDto;
import com.example.bank.api.dto.ClientDto;
import com.example.bank.api.exeptions.NotFoundException;
import com.example.bank.api.factories.ClientDtoFactory;
import com.example.bank.entity.Client;
import com.example.bank.api.exeptions.BadRequestException;
import com.example.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/client")
public class ClientController {

    private final ClientRepository clientRepository;

    private final ClientDtoFactory clientDtoFactory;

    @GetMapping("/client/{id}")
    public ClientDto getClient(@PathVariable Long id) {

        Client client = getClientOrThrowException(id);

        return clientDtoFactory.makeClientDto(client);
    }

    @GetMapping("/str")
    public String getStr() {
        return "client";
    }

    @GetMapping("/all")
    public List<ClientDto> fetchClients(@RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> prefixName.trim().isEmpty());

        Stream<Client> clientStream = optionalPrefixName
                .map(clientRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(clientRepository::streamAllBy);

        return clientStream
                .map(clientDtoFactory::makeClientDto)
                .collect(Collectors.toList());
    }
    @PostMapping("/add")
    public ClientDto addClient(@RequestParam String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty");
        }

        clientRepository
                .findByName(name)
                .ifPresent(client -> {
                            throw new BadRequestException("Client " + name + " already exists");
                        });

        Client client = clientRepository.saveAndFlush(
                Client.builder()
                        .name(name)
                        .build()
        );

        return clientDtoFactory.makeClientDto(client);
    }

    @PatchMapping("/edit/{client_id}")
    public ClientDto editClient(@PathVariable("client_id") Long id,
                                @RequestParam String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty");
        }

        Client client = getClientOrThrowException(id);

        clientRepository
                .findByName(name)
                .filter(client1 -> !Objects.equals(client1.getId(), id))
                .ifPresent(client1 ->{
                    throw new BadRequestException("Client " + name + " already exists");
                });

        client.setName(name);

        client = clientRepository.saveAndFlush(client);

        return clientDtoFactory.makeClientDto(client);
    }

    @DeleteMapping("/delete/{client_id}")
    public AnswerDto deleteClient(@PathVariable("client_id") Long id) {

        Client client = getClientOrThrowException(id);

        clientRepository.deleteById(id);

        return AnswerDto.makeDefault(true);
    }

    @PutMapping("/update")
    public ClientDto updateClient(
            @RequestParam(value = "client_id", required = false) Optional<Long> optionalId,
            @RequestParam(value = "client_name", required = false) Optional<String> optionalName
    ) {

        optionalName = optionalName.filter(clientName -> !clientName.trim().isEmpty());

        boolean isCreate = !optionalId.isPresent();

        Client client = optionalId
                .map(this::getClientOrThrowException)
                .orElseGet(() -> Client.builder().build());

        if (isCreate && !optionalName.isPresent()) {
            throw new BadRequestException("Client name can't be empty");
        }

        optionalName
                .ifPresent(clientName -> {
                    clientRepository
                            .findByName(clientName)
                            .filter(client1 -> !Objects.equals(client1.getId(), client.getId()))
                            .ifPresent(client1 -> {
                                throw new BadRequestException("Client with name " + clientName + " already exists");
                            });
                });

        final Client saveClient = clientRepository.saveAndFlush(client);

        return clientDtoFactory.makeClientDto(saveClient);
    }

    private Client getClientOrThrowException(Long id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Client with id " + id + " doesn't exist")
                );
    }
}
