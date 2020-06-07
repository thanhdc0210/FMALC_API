package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "consignment_status_detail")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsignmentStatusDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "id_consignment", referencedColumnName = "id", insertable = false)
    private Consignment consignment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "id_status", referencedColumnName = "id", insertable = false, updatable = false)
    private ConsignmentStatus status;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private ConsignmentStatus idStatus;

    @Column(name = "time", nullable = false)
    private Timestamp time;
}