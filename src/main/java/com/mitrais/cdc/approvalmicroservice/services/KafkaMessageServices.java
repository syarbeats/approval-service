package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.PostPayload;
import com.mitrais.cdc.approvalmicroservice.repository.BlogApprovalInProgressRepository;
import com.mitrais.cdc.approvalmicroservice.utility.KafkaCustomChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaMessageServices {

    private KafkaCustomChannel kafkaCustomChannel;
    private BlogApprovalInProgressRepository blogApprovalInProgressRepository;

    public KafkaMessageServices(KafkaCustomChannel kafkaCustomChannel,BlogApprovalInProgressRepository blogApprovalInProgressRepository) {
        this.kafkaCustomChannel = kafkaCustomChannel;
        this.blogApprovalInProgressRepository = blogApprovalInProgressRepository;
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
        }

    }

    public void updateBlogStatus(BlogApprovalInProgress blogApprovalInProgress){
        this.kafkaCustomChannel.blogApprovalPubChannel().send(MessageBuilder.withPayload(blogApprovalInProgress).build());
        log.info("Update blog status...");
    }

}
