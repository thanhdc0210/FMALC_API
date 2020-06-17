package fmalc.api.controller;

import fmalc.api.entity.Account;
import java.util.ArrayList;
import java.util.List;
import fmalc.api.dto.AccountDTO;
import fmalc.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(name = "/api/v1.0/accounts", produces =
// MediaType.APPLICATION_JSON_VALUE, consumes =
// MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/api/v1.0/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getListAccount() {
        List<Account> accountList = accountService.findAll();
//        if (accountList.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        List<AccountDTO> responseList = new ArrayList<>(new AccountDTO().mapToListResponse(accountList));
        return ResponseEntity.ok().body(accountList);
    }
}
