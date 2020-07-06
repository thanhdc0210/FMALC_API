package fmalc.api.service;

import fmalc.api.entity.Account;

import java.util.List;

public interface AccountService {
    List<Account> findAll();

    Account updateIsActive(Integer id, Boolean isActive);
}
