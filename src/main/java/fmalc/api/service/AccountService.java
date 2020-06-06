package fmalc.api.service;

import fmalc.api.entities.Account;

import java.util.List;

public interface AccountService {
    List<Account> findAll();
}
