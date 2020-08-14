package fmalc.api.service;

import fmalc.api.entity.AccountNotification;

import java.util.List;

public interface AccountNotificationService {
    List<AccountNotification> findByUsername(String username);
    AccountNotification findByFleetAndNoti(int idFleet, int idNoti);
    AccountNotification save(AccountNotification accountNotification);
}
