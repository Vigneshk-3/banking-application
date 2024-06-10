package com.javaproject.banking.service.impl;

import com.javaproject.banking.dto.AccountDto;
import com.javaproject.banking.entity.Account;
import com.javaproject.banking.mapper.AccountMapper;
import com.javaproject.banking.repository.AccountRepository;
import com.javaproject.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.maptoAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {

//        Account account = accountRepository
//                .findById(id)
//                .orElseThrow(() -> new RuntimeException("Account Does not exists"));
//
//        return AccountMapper.maptoAccountDto(account);

        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()) {
            return null;
        }
        Account account = accountOptional.get();
        return AccountMapper.maptoAccountDto(account);

    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does not exists")); // Getting the account details of a particular id provided

        double total = account.getBalance() + amount; // adding the balance already present and deposited amount.
        account.setBalance(total); // Setting the new balance which is total in the account
        Account savedAccount = accountRepository.save(account); // saving the account details
        return AccountMapper.maptoAccountDto(savedAccount); // Finally returning the accountDto by converting account -> acciuntDto with the help of mapper.
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow( () -> new RuntimeException("Account Does not Exists"));

        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Amount");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.maptoAccountDto(savedAccount);

    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.maptoAccountDto(account))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteAccount(Long id) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow( () -> new RuntimeException("Account Does not Exists"));

        accountRepository.deleteById(id);

    }
}
