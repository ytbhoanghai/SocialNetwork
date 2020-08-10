package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.Account;
import com.nguyenhai.demo.Exception.AccountNotFoundException;
import com.nguyenhai.demo.Exception.EmailExistsException;
import com.nguyenhai.demo.Repository.AccountRepository;
import com.nguyenhai.demo.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "accountService")
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(email));
    }

    @Override
    public Account findById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Account save(Account account) {
        accountRepository.findByEmail(account.getEmail())
                .ifPresent(e -> { throw new EmailExistsException(e.getEmail()); });
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }
}
