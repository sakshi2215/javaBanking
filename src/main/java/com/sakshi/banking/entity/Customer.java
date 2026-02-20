package com.sakshi.banking.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/*
🔹 Basic Fields

customerId → Long
firstName → String
lastName → String
dateOfBirth → LocalDate
gender → Gender (Enum)
phone → String
email → String
kycStatus → KycStatus (Enum)
customerStatus → Status (Enum)
createdAt → LocalDateTime
updatedAt → LocalDateTime
version → Long

🔹 Relationship Fields

addressList → List<Address>
@OneToMany
mappedBy = "customer"
CascadeType.ALL
orphanRemoval = true
FetchType.LAZY

accounts → List<Account>
@OneToMany
mappedBy = "customer"
CascadeType.ALL
orphanRemoval = true
FetchType.LAZY

* */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers",
 indexes = {
        @Index(name = "idx_customer_email", columnList = "email"),
         @Index(name ="idx_customer_phone", columnList = "phone")
 })
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(nullable = false, name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "phone", unique = true, length = 15)
    private String phone;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false)
    private KycStatus kycStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status", nullable = false)
    private Status customerStatus;

    @OneToMany(
            mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY
    )
    private List<Address> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;


    @PrePersist
    public void prePersists(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    //helper methods - For bidirectional relationship
    public void addAddress(Address address){
        addressList.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(Address address){
        addressList.remove(address);
        address.setCustomer(null);
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setCustomer(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setCustomer(null);
    }

}


/*
* In JPA, CascadeType.REMOVE and orphanRemoval = true serve different purposes in a One-to-Many relationship:
*  CascadeType.REMOVE deletes child entities only when the parent entity is deleted, whereas orphanRemoval = true
*  deletes a child entity when it is removed from the parent’s collection, even if the parent itself is not
* deleted. Cascade is triggered by a parent removal operation, while orphan removal is triggered by breaking
* the relationship. In bidirectional relationships, helper methods (like addAddress() and removeAddress())
* are important because JPA does not automatically synchronize both sides; since the owning side (usually
* the @ManyToOne side) controls the foreign key, the helper method ensures both the parent collection and
* the child’s reference are updated together, preventing data inconsistency and hard-to-debug issues.
* */