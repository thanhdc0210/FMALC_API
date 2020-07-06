package fmalc.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import fmalc.api.entity.Account;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@Data
public class AccountDTO {

    private String username;

    private String role;

    public AccountDTO(Account entity) {
        this.username = entity.getUsername();
        this.role = entity.getRole().getRole();
    }

    public List<AccountDTO> mapToListResponse(List<Account> baseEntities) {
        return baseEntities.stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public AccountDTO mapToResponse(Account account) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(account, AccountDTO.class);
    }
}
