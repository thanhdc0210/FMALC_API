package fmalc.api.controller;

import fmalc.api.dto.LocationResponeDTO;
import fmalc.api.dto.NotificationRequestDTO;
import fmalc.api.dto.NotificationResponeDTO;


import fmalc.api.entity.Notification;

import fmalc.api.service.NotificationService;
import fmalc.api.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1.0/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    VehicleService vehicleService;


    // list notify
    private List<NotificationResponeDTO> notificationResponeDTOS = new ArrayList<>();

    // check notify new or old
    private NotificationResponeDTO notificationSend = new NotificationResponeDTO();

    // set 5s to do
    private Flux<Long> intervals = Flux.interval(Duration.ofSeconds(5));


    // save notify and send notify for fleet manager
    @PostMapping("/")
    public ResponseEntity<NotificationResponeDTO> createNotification(@RequestBody NotificationRequestDTO notificationRequestDTO) {
//        NotificationResponeDTO check = null;
        NotificationResponeDTO notificationResponeDTO;
//        String url = "localhost:8082/fmacl/notification/notificationworking";
        try {

            Notification notificationSaved = notificationService.createNotifiation(notificationRequestDTO);
            if (notificationSaved != null) {

                notificationResponeDTO = convertToDto(notificationSaved);
                if (notificationSend != notificationResponeDTO) {
                    notificationResponeDTOS.add(notificationResponeDTO);
//
                    intervals.subscribe((i) -> notifyForManagerWorkingHours());
                    closeInterval();
//       
//                    closeInterval();
//                    System.out.println("LIST" + notificationResponeDTOS.size());
//                    System.out.println("NOTIFY");
//                    notifyForManagerWorkingHours();
                }

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

    private void closeInterval() {
        Disposable disposable = intervals.subscribe();
        disposable.dispose();
    }

    // send notify for fleet manager
    @GetMapping(value = "/notificationworking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<NotificationResponeDTO>> notifyForManagerWorkingHours() {

        intervals.subscribe((i) -> returnResponeFor());
        Flux<List<NotificationResponeDTO>> monoTransaction = Flux.fromStream(Stream.generate(() -> returnResponeFor()));
        Flux<List<NotificationResponeDTO>> flux = Flux.zip(intervals, monoTransaction).map(Tuple2::getT2);

        closeInterval();

        return flux;


    }

    // delete list to disconnect notify
    @GetMapping(value = "/notificationworking/received", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<String> notifyReceived() {

        closeInterval();
//        System.out.println("AAAAAAAAAAA");
        notificationResponeDTOS = new ArrayList<>();
        String result = "OK";

        return ResponseEntity.ok().body(result);
    }


    //convert notify
    private NotificationResponeDTO convertToDto(Notification notify) {
        ModelMapper modelMapper = new ModelMapper();
        NotificationResponeDTO dto = modelMapper.map(notify, NotificationResponeDTO.class);
        return dto;
    }
}
