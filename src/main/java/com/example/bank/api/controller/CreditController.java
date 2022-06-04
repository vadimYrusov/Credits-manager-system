package com.example.bank.api.controller;

import com.example.bank.api.dto.AnswerDto;
import com.example.bank.api.dto.ClientDto;
import com.example.bank.api.dto.CreditDto;
import com.example.bank.api.exeptions.BadRequestException;
import com.example.bank.api.exeptions.NotFoundException;
import com.example.bank.api.factories.CreditDtoFactory;
import com.example.bank.entity.Client;
import com.example.bank.entity.Credit;
import com.example.bank.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/credits")
public class CreditController {

    private final CreditRepository creditRepository;

    private final CreditDtoFactory creditDtoFactory;

    @GetMapping("/all")
    public List<CreditDto> getAllCredits() {

        List<Credit> credits = creditRepository.findAll();

        return credits.isEmpty() ?
                Collections.emptyList() :
                credits.stream()
                        .map(creditDtoFactory::makeCreditDto)
                        .collect(Collectors.toList());
    }

    @GetMapping("/credit/{id}")
    public CreditDto getCreditById(@PathVariable Long id) {

        Credit credit = getCreditOrThrowException(id);

        return creditDtoFactory.makeCreditDto(credit);
    }

    @PostMapping("/add")
    public CreditDto addCredit(@RequestBody Credit credit) {

        if (credit.getName().trim().isEmpty()) {
            throw new BadRequestException("Name can't be empty");
        } else if (credit.getSum() == 0) {
            throw new BadRequestException("Sum can't be 0");
        } else if (credit.getTerm() == 0) {
            throw new BadRequestException("Term can't be 0");
        }

        Credit credit1 = creditRepository.saveAndFlush(
                Credit.builder()
                        .name(credit.getName())
                        .sum(credit.getSum())
                        .term(credit.getTerm())
                        .build()
        );

        return creditDtoFactory.makeCreditDto(credit1);
    }

    @PatchMapping("/edit/{credit_id}")
    public CreditDto editCredit(@PathVariable("credit_id") Long id,
                                @RequestBody Credit credit) {

        Credit credit1 = getCreditOrThrowException(id);

        credit1.setName(credit.getName());
        credit1.setSum(credit.getSum());
        credit1.setTerm(credit.getTerm());

        credit1 = creditRepository.saveAndFlush(credit1);

        return creditDtoFactory.makeCreditDto(credit1);
    }

    @DeleteMapping("/delete/{credit_id}")
    public AnswerDto deleteCredit(@PathVariable("credit_id") Long id) {

        Credit credit = getCreditOrThrowException(id);

        creditRepository.deleteById(id);

        return AnswerDto.makeDefault(true);
    }

    private Credit getCreditOrThrowException(Long id) {
        return creditRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Credit with id " + id + " doesn't exist")
                );
    }


}
