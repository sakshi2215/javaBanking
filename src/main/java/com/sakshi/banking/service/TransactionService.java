package com.sakshi.banking.service;

import com.sakshi.banking.dto.request.DepositRequest;
import com.sakshi.banking.dto.request.TransferRequest;
import com.sakshi.banking.dto.request.WithdrawRequest;
import com.sakshi.banking.dto.response.DepositResponse;
import com.sakshi.banking.dto.response.TransactionResponse;
import com.sakshi.banking.dto.response.TransferResponse;
import com.sakshi.banking.dto.response.WithdrawalResponse;
import com.sakshi.banking.entity.*;
import com.sakshi.banking.exceptions.ResourceNotFoundException;
import com.sakshi.banking.exceptions.account.AccountNotFoundException;
import com.sakshi.banking.exceptions.account.InsufficientFundsException;
import com.sakshi.banking.repository.AccountRepo;
import com.sakshi.banking.repository.TransactionRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*

/**
 * Service responsible for processing monetary transactions
 * such as deposits, withdrawals, and transfers.
 *
 * <p><strong>Domain Responsibility:</strong>
 * Encapsulates all balance-mutating operations and ensures
 * consistency between {@code Account} and {@code Transaction} entities.
 *
 * <p><strong>Concurrency Strategy:</strong>
 * Uses optimistic locking via the {@code @Version} field on the
 * {@code Account} entity to prevent lost updates during concurrent
 * balance modifications.
 *
 * <p><strong>Transaction Management:</strong>
 * Each operation executes within a single database transaction.
 * If any step fails (including optimistic locking conflicts),
 * the entire operation is rolled back automatically.
 *
 * <p><strong>Persistence Rules:</strong>
 * Only successfully completed transactions are persisted.
 * Failed operations do not leave partial updates in the system.
 */
@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;

    public TransactionService(TransactionRepo transactionRepo, AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    /**
     * Processes a deposit request and credits the specified account.
     *
     * <p>
     * The deposit operation performs the following steps:
     * <ul>
     * <li>Retrieves the target account by account number</li>
     * <li>Validates that the account is eligible for deposit</li>
     * <li>Calculates and applies the updated balance</li>
     * <li>Relies on optimistic locking to prevent concurrent update conflicts</li>
     * <li>Creates and persists a SUCCESS transaction record</li>
     * </ul>
     *
     * <p>
     * <strong>Concurrency Behavior:</strong>
     * If another transaction modifies the same account concurrently,
     * an optimistic locking exception is thrown and the operation
     * is rolled back.
     *
     * @param request deposit request containing account number, amount,
     *                and optional description
     * @return DepositResponse containing transaction reference,
     *         updated balance, and transaction metadata
     * @throws AccountNotFoundException                if the account does not exist
     * @throws IllegalStateException                   if the account status
     *                                                 prevents deposits
     * @throws ObjectOptimisticLockingFailureException if a concurrent
     *                                                 modification is detected
     */
    @Transactional
    public DepositResponse deposit(DepositRequest request) {

        Account account = accountRepo.findByAccountNumber(request.getAccountNumber()).orElseThrow(
                () -> new AccountNotFoundException("Account with provided number does not exists"));

        validateAccountStatus(account);

        BigDecimal newBalance = account.getBalance().add(request.getAmount());

        account.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setTranscationType(TransactionType.DEPOSIT);
        transaction.setAmount(request.getAmount());
        transaction.setAccount(account);
        if (request.getDescription() != null)
            transaction.setDescription(request.getDescription());
        transaction.setBalanceAfterTransaction(newBalance);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setReferenceNumber(generateReferenceNumber());
        transactionRepo.save(transaction);

        return mapToDepositResponse(transaction);
    }

    private DepositResponse mapToDepositResponse(Transaction transaction) {
        return DepositResponse.builder()
                .referenceNumber(transaction.getReferenceNumber())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .amount(transaction.getAmount())
                .balanceAfterTransaction(transaction.getBalanceAfterTransaction().toString())
                .transactionStatus(transaction.getTransactionStatus().name())
                .transactionDate(transaction.getCreatedAt())
                .build();
    }

    /**
     * Processes a withdrawal request and debits the specified account.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the target account by account number</li>
     * <li>Validates that the account is active and eligible for withdrawal</li>
     * <li>Checks if the account has sufficient balance</li>
     * <li>Applies the withdrawal amount to the account balance</li>
     * <li>Creates and persists a SUCCESS transaction record</li>
     * </ul>
     *
     * <p>
     * <strong>Concurrency Behavior:</strong>
     * Optimistic locking ensures that concurrent modifications of the account
     * balance are detected. If a concurrent modification occurs, an exception
     * is thrown and the transaction is rolled back.
     *
     * @param request withdrawal request containing account number, amount, and
     *                optional description
     * @return TransactionResponse containing transaction reference, updated
     *         balance, and metadata
     * @throws AccountNotFoundException   if the account does not exist
     * @throws IllegalStateException      if the account is inactive, closed, or
     *                                    blocked
     * @throws InsufficientFundsException if the account balance is insufficient
     */
    @Transactional
    public WithdrawalResponse withdraw(WithdrawRequest request) {

        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        validateAccountStatus(account);

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient balance for withdrawal");
        }

        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance); // optimistic lock will be checked automatically

        Transaction transaction = new Transaction();
        transaction.setTranscationType(TransactionType.WITHDRAWAL);
        transaction.setAmount(request.getAmount());
        transaction.setAccount(account);
        transaction.setDescription(request.getDescription());
        transaction.setBalanceAfterTransaction(newBalance);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setReferenceNumber(generateReferenceNumber());

        transactionRepo.save(transaction);

        return mapToWithdrawalResponse(transaction);
    }

    private WithdrawalResponse mapToWithdrawalResponse(Transaction transaction) {
        return WithdrawalResponse.builder()
                .referenceNumber(transaction.getReferenceNumber())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .amount(transaction.getAmount())
                .balanceAfterTransaction(transaction.getBalanceAfterTransaction().toString())
                .transactionStatus(transaction.getTransactionStatus().name())
                .transactionDate(transaction.getCreatedAt())
                .build();
    }

    /**
     * Transfers funds from one account to another.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves source and destination accounts by account numbers</li>
     * <li>Validates that both accounts are active and eligible for
     * transactions</li>
     * <li>Checks that the source account has sufficient balance</li>
     * <li>Debits the source account and credits the destination account</li>
     * <li>Creates and persists SUCCESS transaction records for both accounts</li>
     * </ul>
     *
     * <p>
     * <strong>Concurrency Behavior:</strong>
     * Optimistic locking ensures that concurrent modifications on either account
     * are detected. If a conflict occurs, the entire transfer is rolled back.
     *
     * @param request transfer request containing source account, destination
     *                account,
     *                amount, and optional description
     * @return TransferResponse containing updated balances and transaction
     *         references
     * @throws AccountNotFoundException   if either account does not exist
     * @throws IllegalStateException      if either account is inactive, closed, or
     *                                    blocked
     * @throws InsufficientFundsException if the source account balance is
     *                                    insufficient
     */
    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        Account source = accountRepo.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account destination = accountRepo.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));

        validateAccountStatus(source);
        validateAccountStatus(destination);

        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Source account has insufficient balance");
        }

        BigDecimal newSourceBalance = source.getBalance().subtract(request.getAmount());
        BigDecimal newDestinationBalance = destination.getBalance().add(request.getAmount());

        source.setBalance(newSourceBalance);
        destination.setBalance(newDestinationBalance);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccount(source);
        debitTransaction.setTranscationType(TransactionType.TRANSFER_DEBIT);
        debitTransaction.setAmount(request.getAmount());
        debitTransaction.setBalanceAfterTransaction(newSourceBalance);
        debitTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        debitTransaction.setReferenceNumber(generateReferenceNumber());
        debitTransaction.setDescription(request.getDescription());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccount(destination);
        creditTransaction.setTranscationType(TransactionType.TRANSFER_CREDIT);
        creditTransaction.setAmount(request.getAmount());
        creditTransaction.setBalanceAfterTransaction(newDestinationBalance);
        creditTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        creditTransaction.setReferenceNumber(generateReferenceNumber());
        creditTransaction.setDescription(request.getDescription());

        transactionRepo.save(debitTransaction);
        transactionRepo.save(creditTransaction);

        return TransferResponse.builder()
                .sourceAccountNumber(source.getAccountNumber())
                .destinationAccountNumber(destination.getAccountNumber())
                .amount(request.getAmount())
                .sourceBalanceAfterTransaction(newSourceBalance)
                .destinationBalanceAfterTransaction(newDestinationBalance)
                .debitTransactionReference(debitTransaction.getReferenceNumber())
                .creditTransactionReference(creditTransaction.getReferenceNumber())
                .build();
    }

    /**
     * Gets all transactions for a customer.
     * 
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Retrieves the account by account number</li>
     * <li>Validates that the account exists</li>
     * <li>Retrieves all transactions for the account</li>
     * </ul>
     * 
     * @param accountNumber unique identifier of the account
     * @return List of TransactionResponse containing transaction details
     * @throws AccountNotFoundException if the account does not exist
     */
    public List<TransactionResponse> getTransactionsByAccountNumber(String accountNumber) {
        Account account = accountRepo.findByAccountNumber(accountNumber).orElseThrow(
                () -> new AccountNotFoundException("Account with provided number does not exists"));

        List<Transaction> transactions = transactionRepo.findByAccount(account);

        // sort transactions by date in descending order
        transactions.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        return transactions.stream().map(transaction -> TransactionResponse.builder()
                .referenceNumber(transaction.getReferenceNumber())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .amount(transaction.getAmount())
                .balanceAfterTransaction(transaction.getBalanceAfterTransaction().toString())
                .transactionStatus(transaction.getTransactionStatus().name())
                .transactionDate(transaction.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() == Status.INACTIVE) {
            throw new IllegalStateException("Account is inactive");
        }
        if (account.getStatus() == Status.CLOSED) {
            throw new IllegalStateException("Account is closed. Contact bank");
        }
        if (account.getStatus() == Status.BLOCKED) {
            throw new IllegalStateException("Account is blocked. Contact bank for assistance");
        }
    }

    public static String generateReferenceNumber() {
        UUID randomUUid = UUID.randomUUID();
        return randomUUid.toString();
    }

}
