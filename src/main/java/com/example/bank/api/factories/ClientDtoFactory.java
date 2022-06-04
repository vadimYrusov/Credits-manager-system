package com.example.bank.api.factories;

import com.example.bank.api.dto.ClientDto;
import com.example.bank.entity.Client;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientDtoFactory {

    private CreditDtoFactory creditDtoFactory;

    public ClientDto makeClientDto(Client client) {

        if (client.getCredits().isEmpty()) {
            return ClientDto.builder()
                    .id(client.getId())
                    .name(client.getName())
                    .surname(client.getSurname())
                    .age(client.getAge())
                    .email(client.getEmail())
                    .build();
        }else {
            return ClientDto.builder()
                    .id(client.getId())
                    .name(client.getName())
                    .surname(client.getSurname())
                    .age(client.getAge())
                    .email(client.getEmail())
                    .credits(
                            client
                                    .getCredits()
                                    .stream()
                                    .map(creditDtoFactory::makeCreditDto)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }


    }

    public ClientDto makeClientDtoWithCredit(Client client) {

        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .age(client.getAge())
                .email(client.getEmail())
                .credits(
                        client
                                .getCredits()
                                .stream()
                                .map(creditDtoFactory::makeCreditDto)
                                .collect(Collectors.toList())
                )
                .build();
    }

}
