package fmalc.api.service;

import fmalc.api.entity.AccountNotification;

import java.util.List;

public interface AccountNotificationService {
    List<AccountNotification> findByUsername(String username);

    Boolean updateStatus(Integer id);
}
