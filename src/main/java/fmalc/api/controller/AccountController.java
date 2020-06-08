package fmalc.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/v1.0/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {
    // @Autowired
    // AccountService accountService;
    //
    // @GetMapping
    // public ResponseEntity<List<AccountResponse>> getListAccount() {
    // List<Account> accountList = accountService.findAll();
    // if (accountList.isEmpty()) {
    // return ResponseEntity.noContent().build();
    // }
    // List<AccountResponse> responseList = new ArrayList<>(new
    // AccountResponse().mapToListResponse(accountList));
    // return ResponseEntity.ok().body(responseList);
    // }
}
