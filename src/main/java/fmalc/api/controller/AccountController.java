package fmalc.api.controller;

import fmalc.api.entities.Account;
import fmalc.api.response.AccountResponse;
import fmalc.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = { "application/json", "application/xml" })
    public ResponseEntity<List<AccountResponse>> getListAccount() {
        List<Account> accountList = accountService.findAll();
        if(accountList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<AccountResponse> responseList = new ArrayList<>(new AccountResponse().mapToListResponse(accountList));
        return   ResponseEntity.ok().body(responseList);
    }
}
