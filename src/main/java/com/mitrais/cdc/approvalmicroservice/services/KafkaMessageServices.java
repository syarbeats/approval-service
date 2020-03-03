package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.*;
import com.mitrais.cdc.approvalmicroservice.repository.BlogApprovalInProgressRepository;
import com.mitrais.cdc.approvalmicroservice.utility.KafkaCustomChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KafkaMessageServices {

    private KafkaCustomChannel kafkaCustomChannel;
    private BlogApprovalInProgressRepository blogApprovalInProgressRepository;
    private ApprovalService approvalService;

    public KafkaMessageServices(KafkaCustomChannel kafkaCustomChannel,BlogApprovalInProgressRepository blogApprovalInProgressRepository, ApprovalService approvalService) {
        this.kafkaCustomChannel = kafkaCustomChannel;
        this.blogApprovalInProgressRepository = blogApprovalInProgressRepository;
        this.approvalService = approvalService;
    }

    @StreamListener("BlogCreationInput")
    public void subscribeBlogCreationMessage(@Payload PostPayload postPayload) {
        log.info("[APPROVAL REQUEST] Receive Blog Creation data:"+postPayload.getTitle());

        if(!blogApprovalInProgressRepository.isExistByTitle(postPayload.getTitle())){
            BlogApprovalInProgress blogApprovalInProgress = new BlogApprovalInProgress();
            blogApprovalInProgress.setTitle(postPayload.getTitle());
            blogApprovalInProgress.setCategoryId(postPayload.getCategoryId());
            blogApprovalInProgress.setCategoryName(postPayload.getCategoryName());
            blogApprovalInProgress.setCreatedDate(postPayload.getCreatedDate());
            blogApprovalInProgress.setSummary(postPayload.getSummary());
            blogApprovalInProgress.setApprovalProgress("To Do");
            blogApprovalInProgressRepository.save(blogApprovalInProgress);
            this.sendBlogCreatedEvent(blogApprovalInProgress);
            this.sendBlogCreatedEventV2(blogApprovalInProgress);
            this.sendBlogApprovalEvent(blogApprovalInProgressRepository.getApprovalStatistic());
            this.sendBlogApprovalV2Event(approvalService.getApprovalStatistic());
        }

    }

    public void updateBlogStatus(BlogApprovalInProgress blogApprovalInProgress){
        this.kafkaCustomChannel.blogApprovalPubChannel().send(MessageBuilder.withPayload(blogApprovalInProgress).build());
        log.info("Update blog status...");
    }

    public void sendUpdateProgressNotification(BlogApprovalInProgress blogApprovalInProgress){
        this.kafkaCustomChannel.blogApprovalNotificationPubChannel().send(MessageBuilder.withPayload(blogApprovalInProgress).build());
        log.info("Sent update event to update progress channel");
    }

    public void sendKey(Key key){
        this.kafkaCustomChannel.blogPubKey().send(MessageBuilder.withPayload(key).build());
        log.info("Sent key event...");
    }

    public void sendBlogCreatedEvent(BlogApprovalInProgress blogApprovalInProgress){
        this.kafkaCustomChannel.blogNumberPerCategoryPubChannel().send(MessageBuilder.withPayload(blogApprovalInProgress).build());
    }

    public void sendBlogCreatedEventV2(BlogApprovalInProgress blogApprovalInProgress){
        this.kafkaCustomChannel.blogNumberPerCategoryPubChannelV2().send(MessageBuilder.withPayload(blogApprovalInProgress).build());
    }

    public void sendBlogApprovalEvent(List<ApprovalNumberPerProgress> approvalNumberPerProgress){
        this.kafkaCustomChannel.blogApprovalStatisticOutput().send(MessageBuilder.withPayload(approvalNumberPerProgress).build());
    }

    public void sendBlogApprovalV2Event(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses){
        this.kafkaCustomChannel.blogApprovalStatisticV2Output().send(MessageBuilder.withPayload(approvalNumberPerProgressResponses).build());
    }

    public void sendApprovalResultStatistic(List<ApprovalResultStatistic> approvalResultStatistics){
        this.kafkaCustomChannel.blogApprovalResultStatisticOutput().send(MessageBuilder.withPayload(approvalResultStatistics).build());
    }

}
