package com.example.bank.service;

import com.example.bank.entity.Client;
import com.example.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    @Override
    public Client getClient(Long id) {
        return clientRepository.getClientById(id);
    }
}
