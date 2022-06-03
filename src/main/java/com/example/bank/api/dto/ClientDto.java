package com.example.bank.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long id;

    private String name;

    private String surname;

    private int age;

    private String email;

    private List<CreditDto> credits = new ArrayList<>();
}
