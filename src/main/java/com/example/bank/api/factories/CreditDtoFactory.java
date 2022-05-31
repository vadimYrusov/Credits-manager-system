package com.example.bank.api.factories;

import com.example.bank.api.dto.ClientDto;
import com.example.bank.api.dto.CreditDto;
import com.example.bank.entity.Client;
import com.example.bank.entity.Credit;
import org.springframework.stereotype.Component;

@Component
public class CreditDtoFactory {

    public CreditDto makeCreditDto(Credit credit) {

        return CreditDto.builder()
                .id(credit.getId())
                .name(credit.getName())
                .sum(credit.getSum())
                .term(credit.getTerm())
                .build();
    }
}
