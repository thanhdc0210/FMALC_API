//package fmalc.api.entity;
//
//import java.io.Serializable;
//import javax.persistence.*;
//
//import lombok.*;
//import org.hibernate.annotations.GenericGenerator;
//
//@Entity
//@Table(name = "notify_type")
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class NotifyType implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GenericGenerator(name = "generator", strategy = "native")
//    @GeneratedValue(generator = "generator", strategy= GenerationType.AUTO)
//    @Column(name = "id")
//    private Integer id;
//
//    @Column(name = "notify_type_name", nullable = false)
//    private String notifyTypeName;
//
//}