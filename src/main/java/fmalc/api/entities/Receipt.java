package fmalc.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "receipt")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "place_of_receipt", nullable = false)
    private String placeOfReceipt;

    @Column(name = "time_receipt", nullable = false)
    private Timestamp timeReceipt;

}