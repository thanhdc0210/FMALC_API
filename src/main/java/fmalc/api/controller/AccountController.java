package fmalc.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fmalc.api.entities.Account;
import fmalc.api.response.AccountResponse;
import fmalc.api.service.AccountService;

@RestController
// @RequestMapping(name = "/api/v1.0/accounts", produces =
// MediaType.APPLICATION_JSON_VALUE, consumes =
// MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getListAccount() {
        List<Account> accountList = accountService.findAll();
        if (accountList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<AccountResponse> responseList = new ArrayList<>(new AccountResponse().mapToListResponse(accountList));
        return ResponseEntity.ok().body(responseList);
    }
}
