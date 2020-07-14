package fmalc.api.service.impl;

import fmalc.api.dto.PlaceResponeDTO;
import fmalc.api.entity.Consignment;
import fmalc.api.entity.DayOff;
import fmalc.api.entity.Driver;
import fmalc.api.enums.TypeLocationEnum;
import fmalc.api.repository.DayOffRepository;
import fmalc.api.service.DayOffService;
import fmalc.api.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DayOffServiceImpl implements DayOffService {

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    PlaceService placeService;

    @Override
    public List<Driver> checkDayOffODriver(List<Driver> idDriver, Consignment consignment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Driver> drivers = new ArrayList<>();
        List<DayOff> dayOffs  = new ArrayList<>();
        boolean flag = true;
        for(int i= 0; i<idDriver.size(); i++){
            flag = true;
            dayOffs = dayOffRepository.checkDayOffOfDriver(idDriver.get(i).getId());
            if(dayOffs.size()>0){
                for (int j = 0; j< dayOffs.size(); j++){
                    String dateOff = sdf.format(dayOffs.get(j).getStartDate());

                    PlaceResponeDTO placeSchedulePriorityRecei =
                            placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), 1, TypeLocationEnum.RECEIVED_PLACE.getValue());

                    List<PlaceResponeDTO> placeConsgimentsPriorityDeli =
                            placeService.getPlaceByTypePlace(consignment.getId(), TypeLocationEnum.DELIVERED_PLACE.getValue());

                    if(placeSchedulePriorityRecei !=null){
                        String dateReceiOfConsignment = sdf.format(placeSchedulePriorityRecei.getPlannedTime());
                        if(dateOff.compareTo(dateReceiOfConsignment)>=1){
                            PlaceResponeDTO placeSchedulePriorityDeli =
                                    placeService.getPlaceByTypePlaceAndPriority(consignment.getId(), placeConsgimentsPriorityDeli.size(), TypeLocationEnum.DELIVERED_PLACE.getValue());
                            if(placeSchedulePriorityDeli!=null){
                                String dateDeliOfConsignment = sdf.format(placeSchedulePriorityDeli.getPlannedTime());
                                if(dateOff.compareTo(dateDeliOfConsignment)>=1){

                                }else{
                                    flag = false;
                                }
                            }else{
//
                            }

                        }else if(dateOff.compareTo(dateReceiOfConsignment)<=-1){
                            String dateEnd = sdf.format(dayOffs.get(j).getEndDate());
                            if(dateEnd.compareTo(dateReceiOfConsignment) >= 1){
                                flag = false;
                            }
                        }
                    }else{

                    }
                    if(flag == false){
                        j = dayOffs.size();
                    }else{

                    }
                }
                if(flag == true){
                    drivers.add(idDriver.get(i));
                }

//                String dateConsignment =
            }else{
                drivers.add(idDriver.get(i));
            }
        }
        return drivers;
    }
}
