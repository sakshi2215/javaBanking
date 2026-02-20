package com.sakshi.banking.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_tx__account", columnList = "account_id"),
                @Index(name = "idx_tx_reference", columnList = "reference_number")
        }
)

/*
* 🔹 Basic Fields
id → Long
transcationType → TranscationType (Enum)
amount → BigDecimal
transactionDate → LocalDateTime
referenceNumber → String
description → String
transactionStatus → TransactionStatus (Enum)
balanceAfterTransaction → BigDecimal
createdAt → LocalDateTime
updatedAt → LocalDateTime
version → Long

🔹 Relationship Field
account → Account
@ManyToOne
Foreign key: account_id
FetchType.LAZY*/

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transcationType;

    @Column(name = "amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;

    @Column(name= "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "balance_after_transaction",precision = 19, scale = 4, nullable = false )
    private BigDecimal balanceAfterTransaction;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;


    @PrePersist
    public void prePersists(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.transactionDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
