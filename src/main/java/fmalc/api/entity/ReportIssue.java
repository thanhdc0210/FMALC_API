package fmalc.api.entity;

import java.awt.print.Book;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "report_issue")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
// Báo cáo tình trạng bên ngoài xe trước và sau khi chạy
public class ReportIssue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private Driver createdBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Driver updatedBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "inspection_id", referencedColumnName = "id", nullable = false)
    private Inspection inspection;

    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "content")
    private String content;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "image", length = 1000)
    private String image;

    // Báo cáo trước khi chạy hay sau khi chạy hoàn thành đơn hàng
    @Column(name = "type", nullable = false)
    private Integer type;
}