package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.payload.BlogNumberPerCategory;
import com.mitrais.cdc.approvalmicroservice.payload.BlogStatistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public void sendObject(List<BlogStatistic> blogStatisticList, String mode){
        this.template.convertAndSend("/topic/statistic", blogStatisticList);
    }

    public void sendBlogAmountPerCategoryMessage(List<BlogNumberPerCategory> blogNumberPerCategoryList, String mode){
        this.template.convertAndSend("/topic/blog-number", blogNumberPerCategoryList);
    }
}

