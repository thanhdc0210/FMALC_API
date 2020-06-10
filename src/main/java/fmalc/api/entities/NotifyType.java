package fmalc.api.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "notify_type")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @Column(name = "notify_type_name", nullable = false)
    private String notifyTypeName;

}