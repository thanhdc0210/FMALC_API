package fmalc.api.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "maintain_type")
public class MaintainType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "content", nullable = false)
    private String content;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "kilometers_number", nullable = false)
    private Integer kilometersNumber;

    @Column(name = "maintain_type_name", nullable = false)
    private String maintainTypeName;

    
}