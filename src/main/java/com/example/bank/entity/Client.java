package com.example.bank.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "clients_credits",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "credit_id")
    )
    private Set<Credit> credits = new HashSet<>();

    public void addCredit(Credit credit) {
        credits.add(credit);
        credit.getClients().add(this);
    }

    public void removeCredit(Credit credit) {
        credits.remove(credit);
        credit.getClients().remove(this);
    }
}
