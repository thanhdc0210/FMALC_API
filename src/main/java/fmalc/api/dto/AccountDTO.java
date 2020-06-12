package fmalc.api.dto;

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
public class AccountDTO {

    private String username;

    @JsonProperty("roleAccount")
    private String role;

    public AccountDTO(Account entity) {
        this.username = entity.getUsername();
        this.role = entity.getRole().getRole();
    }

    public List<AccountDTO> mapToListResponse(List<Account> baseEntities) {
        return baseEntities
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

}
