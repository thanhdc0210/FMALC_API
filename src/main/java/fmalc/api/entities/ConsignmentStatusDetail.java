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
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "consignment_id", referencedColumnName = "id", insertable = false)
    private Consignment consignment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "status_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ConsignmentStatus status;

    @Column(name = "time", nullable = false)
    private Timestamp time;
}