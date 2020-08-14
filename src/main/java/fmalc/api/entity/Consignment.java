package fmalc.api.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "consignment")
public class Consignment implements Serializable {
    private static final long serialVersionUID = 1L;

    // Số lượng kiện hàng
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
        private Collection<ConsignmentHistory> consignmentHistories;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_note")
    private String ownerNote;

    // Tổng trọng lượng
    @Column(name = "weight", nullable = false)
    private Double weight;
    @Column(name = "cancel_note")
    private String cancelNote;
    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "consignment")
    private Collection<Schedule> schedules;

    @OneToMany(mappedBy = "consignment", cascade = { CascadeType.MERGE})
    private Collection<Place> places;

    @Column(name = "image_contract", length = 2000)
    private String imageContract;

    public Timestamp getStartDateForReport(){
        return places.stream().sorted(Comparator.comparing(Place::getActualTime)).collect(Collectors.toList())
                .stream().findFirst().orElse(null).getActualTime();

    }
}