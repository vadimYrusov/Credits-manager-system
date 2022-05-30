package com.example.bank.entity;

import lombok.*;

import javax.persistence.*;
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

    @ManyToMany(mappedBy = "credits")
    private Set<Client> clients;
}
