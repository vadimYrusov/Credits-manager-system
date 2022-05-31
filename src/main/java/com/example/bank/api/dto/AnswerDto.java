package com.example.bank.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    Boolean answer;

    public static AnswerDto makeDefault(Boolean answer) {
        return builder()
                .answer(answer)
                .build();
    }
}
