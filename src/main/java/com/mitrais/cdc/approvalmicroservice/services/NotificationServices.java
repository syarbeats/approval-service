package com.mitrais.cdc.approvalmicroservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class NotificationServices {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Autowired
    private SimpMessagingTemplate template;

    public void sendMessageTo(String topic, String message) {
        log.info("Send Message to websocket...");
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(dateFormatter.format(new Date()));
        builder.append("] ");
        builder.append(message);
        this.template.convertAndSend("/topic/" + topic, builder.toString());
    }

    public void sendMessage(String message){
        log.info("Send Message to websocket...after status updated...");
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        this.template.convertAndSend("/topic/message", builder.toString());
    }
}
