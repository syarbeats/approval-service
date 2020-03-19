package com.mitrais.cdc.approvalmicroservice.controller;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgressResponse;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalResultStatistic;
import com.mitrais.cdc.approvalmicroservice.services.KafkaMessageServices;
import com.mitrais.cdc.approvalmicroservice.services.NotificationServices;
import com.mitrais.cdc.approvalmicroservice.services.ReactiveApprovalServices;
import com.mitrais.cdc.approvalmicroservice.utility.UserContextHolder;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReactiveApprovalController {

    private ReactiveApprovalServices reactiveApprovalServices;
    private KafkaMessageServices kafkaMessageServices;
    private NotificationServices notificationServices;

    public ReactiveApprovalController(ReactiveApprovalServices reactiveApprovalServices, KafkaMessageServices kafkaMessageServices, NotificationServices notificationServices) {
        this.reactiveApprovalServices = reactiveApprovalServices;
        this.kafkaMessageServices = kafkaMessageServices;
        this.notificationServices = notificationServices;
    }

    @GetMapping("/approval-list-reactive")
    public Single<ResponseEntity<List<BlogApprovalInProgress>>> getAllBlogApprovalData(@RequestParam("approval") String approvalProgess, Pageable pageable){
        return reactiveApprovalServices.getAllApprovalBlogData(approvalProgess, pageable)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload.getContent()));
    }

    @PostMapping("/process-reactive")
    public Single<ResponseEntity<BlogApprovalInProgress>> updateProgressStatus(@RequestParam("id") Long id, @RequestParam("status") String status, @RequestParam("progress") String progress){
        return reactiveApprovalServices.updateProgressStatus(id, status, progress)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));

    }

    @GetMapping("/approval-statistic-reactive")
    public Single<ResponseEntity<List<ApprovalNumberPerProgressResponse>>> getApprovalStatistic(){
        return reactiveApprovalServices.getApprovalStatistic()
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/approval-result-statistic-reactive")
    public Single<ResponseEntity<List<ApprovalNumberPerProgressResponse>>> getApprovalResultStatistic(){
        return reactiveApprovalServices.getApprovalResultStatistic()
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/approval-statistic-v2-reactive")
    public Single<ResponseEntity<List<ApprovalNumberPerProgress>>> getApprovalStatisticV2(){
        return reactiveApprovalServices.getApprovalStatistiV2()
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/approval-result-statistic-v2-reactive")
    public Single<ResponseEntity<List<ApprovalResultStatistic>>> getApprovalResultStatisticV2(){
        return reactiveApprovalServices.getApprovalResultStatisticV2()
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }
}
