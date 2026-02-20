package com.sakshi.banking.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "atm")
public class ATM {
    @Id
    @Column(name = "atm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long atmId;

    @Column(nullable = false, name = "location", length = 100)
    private String location;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private ATMStatus status;

    // getters and setters
}