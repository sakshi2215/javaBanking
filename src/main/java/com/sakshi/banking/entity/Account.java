package com.sakshi.banking.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/*
 * account_id (PK)
 * account_number (unique)
 * account_type (Savings, Current, Fixed)
 * balance
 * opened_date
 * status
 * customer_id (FK)
 */

@Entity
@Getter
@Setter
@Table(name = "accounts",
indexes = {
        @Index(name = "idx_account_number", columnList = "account_number")
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private AccountType accountType;

    @Column(name = "balance", precision = 19, scale = 4, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "opened_date", nullable = false, unique = false)
    private LocalDateTime openedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

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
        if (this.openedDate == null) {
            this.openedDate = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}

/*

Primary Key → @Id with GenerationType.IDENTITY.
Account Number → Unique and indexed.
Account Type & Status → Enums for safer values.
Balance → BigDecimal with precision & default 0.
Opened Date → Auto-set on persist if not provided.
Auditing → createdAt and updatedAt with @PrePersist & @PreUpdate.
Optimistic Locking → @Version prevents concurrent update conflicts.
Relationship to Customer → @ManyToOne with lazy fetch.*/
