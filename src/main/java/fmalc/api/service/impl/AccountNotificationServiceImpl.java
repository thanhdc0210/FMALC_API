package fmalc.api.service.impl;

import fmalc.api.entity.AccountNotification;
import fmalc.api.repository.AccountNotificationRepository;
import fmalc.api.service.AccountNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AccountNotificationServiceImpl implements AccountNotificationService {

    @Autowired
    AccountNotificationRepository accountNotificationRepository;
    @Override
    public List<AccountNotification> findByUsernameAndTime(String username) {
        try {
            return accountNotificationRepository.findByUsernameAndTime(username,
                    Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MIN)),
                    Timestamp.valueOf(LocalDateTime.now().with(LocalTime.MAX)));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public AccountNotification findByFleetAndNoti(int idFleet, int idNoti) {
        return accountNotificationRepository.getAccountNotiByFleetAndNoti(idFleet,idNoti);
    }

    @Override
    public AccountNotification save(AccountNotification accountNotification) {
        return accountNotificationRepository.save(accountNotification);
    }

}
