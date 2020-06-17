package fmalc.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatusRequestDTO {
    private String username;
    private List<Integer> status;
}
