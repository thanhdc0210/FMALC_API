package fmalc.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NotificationData {

    // Loại thông báo
    private String type;

    private String title;

    private String body;

    // Có value nếu noti cho schedule hoặc maintain
    private String id;
}
