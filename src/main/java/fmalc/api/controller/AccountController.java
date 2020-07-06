package fmalc.api.controller;

import fmalc.api.dto.DriverResponseDTO;
import fmalc.api.entity.Account;
import java.util.ArrayList;
import java.util.List;
import fmalc.api.dto.AccountDTO;
import fmalc.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1.0/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getListAccount() {
        List<Account> accountList = accountService.findAll();
        if (accountList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<AccountDTO> responseList = new ArrayList<>(new AccountDTO().mapToListResponse(accountList));
        return ResponseEntity.ok().body(responseList);
    }

    @PutMapping(value = "is-active/{id}")
    public ResponseEntity updateIsActive(@PathVariable("id") Integer id, @RequestParam("isActive") Boolean isActive) {
        try {
            Account account = accountService.updateIsActive(id, isActive);
            return ResponseEntity.ok().body(new AccountDTO().mapToResponse(account));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
