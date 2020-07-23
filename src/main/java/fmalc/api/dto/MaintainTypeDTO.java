package fmalc.api.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class MaintainTypeDTO {
    private Integer id;


    private String content;

    // Bảo trì khi đạt 5000km hoặc 10000km

    private Integer kilometersNumber;

    private String maintainTypeName;
}
