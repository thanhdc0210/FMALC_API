package fmalc.api.dto;

import fmalc.api.entity.Notification;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class DayOffNotificationResponseDTO {
    private Integer id;
    private Date startDate;
    private Date endDate;
    private DriverResponseForNotifyDTO driver;
    private String note;
    private Boolean isApprove;
    private Integer type;

    public DayOffNotificationResponseDTO mapToResponse(Notification notification) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        DayOffNotificationResponseDTO dto = modelMapper.map(notification, DayOffNotificationResponseDTO.class);
        if (notification.getType() == 5) {
            dto.setNote(notification.getContent());
            dto.setStartDate(new Date(notification.getTime().getTime()));
            dto.setEndDate(new Date(notification.getTime().getTime()));
        }
        if (notification.getType() == 4) {
            List<String> str = Pattern.compile("\\|")
                    .splitAsStream(notification.getContent())
                    .collect(Collectors.toList());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dto.setStartDate(simpleDateFormat.parse(str.get(0)));
            dto.setEndDate(simpleDateFormat.parse(str.get(1)));
            dto.setNote("");
        }
        dto.setIsApprove(false);
        return dto;
    }

    public List<DayOffNotificationResponseDTO> mapToListResponse(List<Notification> notifications) {
        return notifications
                .stream()
                .map(notification -> {
                    try {
                        return mapToResponse(notification);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
}
