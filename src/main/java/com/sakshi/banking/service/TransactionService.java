package com.sakshi.banking.service;
import com.sakshi.banking.dto.request.DepositRequest;
import com.sakshi.banking.dto.response.DepositResponse;
import com.sakshi.banking.entity.Account;
import com.sakshi.banking.entity.Status;
import com.sakshi.banking.exceptions.ResourceNotFoundException;
import com.sakshi.banking.repository.AccountRepo;
import com.sakshi.banking.repository.TransactionRepo;
import org.springframework.stereotype.Service;


/*

WILL FINISH TOMORROW

- Add amount to balance
- Create Transaction entity
- Save transaction
- Update account balance
- Return TransactionResponse*/
@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;

    public TransactionService(TransactionRepo transactionRepo, AccountRepo accountRepo){
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }
    public DepositResponse deposit(DepositRequest request){

        Account account = accountRepo.findByAccountNumber(request.getAccountNumber()).orElseThrow(
                ()-> new ResourceNotFoundException("Account with provided number does not exists")
        );

        if(account.getStatus()== Status.INACTIVE) throw new RuntimeException("Account is Inactive");
        if(account.getStatus()==Status.CLOSED) throw new ResourceNotFoundException("Account is closed, Contact bank");
        if(account.getStatus()==Status.BLOCKED) throw  new RuntimeException("Account has been blocked, Contact bank for further Assistance");



        return new DepositResponse();
    }
}
