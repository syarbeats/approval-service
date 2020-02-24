package com.mitrais.cdc.approvalmicroservice.controller;

import com.mitrais.cdc.approvalmicroservice.services.NotificationServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController extends CrossOriginController{

    private NotificationServices notificationServices;

    public NotificationController(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }

    @RequestMapping("/send/{topic}")
    public String sender(@PathVariable String topic, @RequestParam String message){
        notificationServices.sendMessageTo(topic, message);
        return "OK-Sent";
    }
}
