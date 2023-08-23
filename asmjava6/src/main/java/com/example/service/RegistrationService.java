
package com.example.service;

import com.example.entity.Account;
import com.example.jparepository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final AccountRepository accountRepository;

    @Autowired
    public RegistrationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) {
        // Additional validation and logic can be added here before saving the account
        return accountRepository.save(account);
    }
}
