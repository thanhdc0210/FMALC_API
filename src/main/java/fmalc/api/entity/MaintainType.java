package fmalc.api.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "maintain_type")
public class MaintainType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "kilometers_number", nullable = false)
    private Integer kilometersNumber;

    @Column(name = "maintain_type_name", nullable = false)
    private String maintainTypeName;

}