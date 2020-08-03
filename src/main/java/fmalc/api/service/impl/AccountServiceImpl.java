package fmalc.api.service.impl;

import fmalc.api.entity.Account;
import fmalc.api.repository.AccountRepository;
import fmalc.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateIsActive(Integer id, Boolean isActive) {
        accountRepository.updateIsActiveById(id, isActive);
        return accountRepository.findById(id).get();
    }

    @Override
    public Account getAccount(String username) {
        return accountRepository.findByUsername(username);
    }
}
