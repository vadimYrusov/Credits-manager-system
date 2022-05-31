package com.example.bank.api.factories;

import com.example.bank.api.dto.ClientDto;
import com.example.bank.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoFactory {

    public ClientDto makeClientDto(Client client) {

        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .age(client.getAge())
                .email(client.getEmail())
                .build();
    }

}
