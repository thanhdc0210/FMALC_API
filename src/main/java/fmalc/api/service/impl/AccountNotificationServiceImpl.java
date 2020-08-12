package fmalc.api.service.impl;

import fmalc.api.entity.AccountNotification;
import fmalc.api.repository.AccountNotificationRepository;
import fmalc.api.service.AccountNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountNotificationServiceImpl implements AccountNotificationService {

    @Autowired
    AccountNotificationRepository accountNotificationRepository;
    @Override
    public List<AccountNotification> findByUsername(String username) {
        try {
            return accountNotificationRepository.findByUsername(username);
        }catch (Exception e){
            return null;
        }
    }

}
