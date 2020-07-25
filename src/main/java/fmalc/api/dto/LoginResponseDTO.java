package fmalc.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO implements Serializable {
    private String username;
    private String token;
    private String role;
    private String avatar;
    private String name;

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", role='" + role + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
