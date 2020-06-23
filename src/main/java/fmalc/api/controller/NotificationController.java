package fmalc.api.controller;

import fmalc.api.dto.LocationResponeDTO;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;

import fmalc.api.entity.Location;
import fmalc.api.entity.Notify;
import fmalc.api.service.NotificationService;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;

import javax.management.Notification;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    VehicleService vehicleService;

    private List<NotificationResponeDTO> notificationResponeDTOS = new ArrayList<>();

    private NotificationResponeDTO notificationSend = new NotificationResponeDTO();

    @PostMapping("/")
    public ResponseEntity<NotificationResponeDTO> createNotification(@RequestBody NotificationRequestDTO notificationRequestDTO) {
        NotificationResponeDTO check = null;
        String url = "localhost:8082/fmacl/notification/notificationworking";
        try {

            Notify notificationSaved = notificationService.createNotifiation(notificationRequestDTO);
            if (notificationSaved != null) {

                NotificationResponeDTO notificationResponeDTO = convertToDto(notificationSaved);
                if (notificationSend != notificationResponeDTO) {
                    notificationResponeDTOS.add(notificationResponeDTO);
                    notificationSend = notificationResponeDTO;
//                    Flux<List<NotificationResponeDTO>> flux =
//                    notifyForManagerWorkingHours();
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getForObject(url,String.class);
                    notifyForManagerWorkingHours(notificationResponeDTO);
                }
//
                check = notificationResponeDTO;
//                    if()
                notificationSend = notificationResponeDTO;
//                }

                return ResponseEntity.ok().body(notificationResponeDTO);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    private List<NotificationResponeDTO> returnResponeFor() {
        return notificationResponeDTOS;
    }

    private void lll(Flux<Long> intervals) {
        notificationResponeDTOS = new ArrayList<>();
        Disposable disposable = intervals.subscribe();
        disposable.dispose();
    }

    //
    @GetMapping(value = "/notificationworking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotificationResponeDTO> notifyForManagerWorkingHours(NotificationResponeDTO notificationResponeDTO) {

        Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));
//        intervals.subscribe((i) -> returnResponeFor());
        Flux<NotificationResponeDTO> monoTransaction = Flux.fromStream(Stream.generate(() -> notificationResponeDTO));

        Flux<NotificationResponeDTO> flux = Flux.zip(intervals, monoTransaction).map(Tuple2::getT2);
//        if (returnResponeFor().size() <= 0) {
//            lll(intervals);
////            lll(intervals);
//        } else {
//            lll(intervals);

//            Date timeToRun = new Date(System.currentTimeMillis() + (1000 * 10));
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                public void run() {
//                    notificationResponeDTOS = new ArrayList<>();
//                    Disposable disposable = intervals.subscribe();
//                    disposable.dispose();
//                }
//            }, timeToRun);

//        }
        System.out.println("NOTIFY");
        return flux;

//        return (Mono<ServerResponse>) ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(notificationResponeDTO, NotificationResponeDTO.class);


    }

    private NotificationResponeDTO convertToDto(Notify notify) {
        ModelMapper modelMapper = new ModelMapper();
        NotificationResponeDTO dto = modelMapper.map(notify, NotificationResponeDTO.class);

        return dto;
    }
}
