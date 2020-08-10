package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.Account;

public interface AccountService {

    Account findByEmail(String email);

    Account findById(String id);

    Account save(Account account);

    Account update(Account account);
}
