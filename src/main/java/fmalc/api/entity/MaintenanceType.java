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
@Table(name = "maintenance_type")
public class MaintenanceType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content")
    private String content;

    // Bảo trì khi đạt 5000km hoặc 10000km
    @Column(name = "kilometers_number", nullable = false)
    private Integer kilometersNumber;

    @Column(name = "maintenance_type_name", nullable = false)
    private String maintenanceTypeName;

}