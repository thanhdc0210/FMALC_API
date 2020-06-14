package fmalc.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountDTO {
    private Integer id;
    private String username;
    private RoleDTO role;
}
