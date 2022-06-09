package com.example.bank.repository;

import com.example.bank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
