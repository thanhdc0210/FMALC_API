package fmalc.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fmalc.api.entities.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponse {

    private String username;

    @JsonProperty("roleAccount")
    private String role;

    public AccountResponse(Account entity) {
        this.username = entity.getUsername();
        this.role = entity.getRole().getRole();
    }

    public List<AccountResponse> mapToListResponse(List<Account> baseEntities) {
        return baseEntities
                .stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

}
