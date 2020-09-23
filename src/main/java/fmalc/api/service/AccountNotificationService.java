package fmalc.api.service;

import fmalc.api.entity.AccountNotification;

import java.sql.Timestamp;
import java.util.List;

public interface AccountNotificationService {
    List<AccountNotification> findByUsernameAndTime(String username);
    AccountNotification findByFleetAndNoti(int idFleet, int idNoti);
    AccountNotification save(AccountNotification accountNotification);
}
