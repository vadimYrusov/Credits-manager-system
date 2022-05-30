package com.example.bank.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "credit")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long sum;

    private int term;
}
