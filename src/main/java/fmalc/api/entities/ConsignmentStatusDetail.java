package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;

@Table(name = "consignment_status_detail")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsignmentStatusDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "id_consignment", nullable = false)
    @ManyToOne
    private Consignment idConsignment;

    @JoinColumn(name = "id_status", nullable = false)
    @ManyToOne
    private ConsignmentStatus idStatus;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    
}