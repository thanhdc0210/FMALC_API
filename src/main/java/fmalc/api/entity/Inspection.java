package fmalc.api.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inspection")
public class Inspection implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "inspection_name", nullable = false, unique = true)
    private String inspectionName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}