package com.mitrais.cdc.approvalmicroservice.controller;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgressResponse;
import com.mitrais.cdc.approvalmicroservice.payload.Key;
import com.mitrais.cdc.approvalmicroservice.services.ApprovalService;
import com.mitrais.cdc.approvalmicroservice.services.KafkaMessageServices;
import com.mitrais.cdc.approvalmicroservice.services.NotificationServices;
import com.mitrais.cdc.approvalmicroservice.utility.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApprovalController extends CrossOriginController{

    private ApprovalService approvalService;
    private KafkaMessageServices kafkaMessageServices;

    @Autowired
    private NotificationServices notificationServices;


    public ApprovalController(ApprovalService approvalService, KafkaMessageServices kafkaMessageServices) {
        this.approvalService = approvalService;
        this.kafkaMessageServices = kafkaMessageServices;
    }

    @GetMapping("/approval-list")
    public ResponseEntity<List<BlogApprovalInProgress>> getAllBlogApprovalData(@RequestParam("approval") String approvalProgess, Pageable pageable){
        return ResponseEntity.ok(approvalService.getAllApprovalBlogData(approvalProgess,pageable).getContent());
    }

    @PostMapping("/process")
    public ResponseEntity<BlogApprovalInProgress> updateProgressStatus(@RequestParam("id") Long id, @RequestParam("status") String status, @RequestParam("progress") String progress){

        BlogApprovalInProgress blogApprovalInProgress = approvalService.updateProgressStatus(id, status, progress);;

        /*
        * Only send Update Status event to Message Broker
        * when status either approved or rejected with progress Done
        * **/

        if((status.equals("Approved") && progress.equals("Done")) || (status.equals("Rejected") && progress.equals("Done")) ){
            kafkaMessageServices.updateBlogStatus(blogApprovalInProgress);
        }

        log.info("TOKEN[APPROVAL]:"+ UserContextHolder.getContext().getAuthToken());
        kafkaMessageServices.sendKey(new Key(UserContextHolder.getContext().getAuthToken()));
        kafkaMessageServices.sendUpdateProgressNotification(blogApprovalInProgress);

        return ResponseEntity.ok(blogApprovalInProgress);
    }

    @GetMapping("/approval-statistic")
    public ResponseEntity<List<ApprovalNumberPerProgressResponse>> getApprovalStatistic(){
        return ResponseEntity.ok(approvalService.getApprovalStatistic());
    }

    @GetMapping("/approval-result-statistic")
    public ResponseEntity<List<ApprovalNumberPerProgressResponse>> getApprovalResultStatistic(){
        return ResponseEntity.ok(approvalService.getApprovalResultStatistic());
    }
}
