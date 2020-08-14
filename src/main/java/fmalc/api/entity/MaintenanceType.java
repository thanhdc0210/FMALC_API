package fmalc.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "maintenance_type")
public class MaintenanceType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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