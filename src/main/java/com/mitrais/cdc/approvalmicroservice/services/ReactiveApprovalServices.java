package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgressResponse;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalResultStatistic;
import com.mitrais.cdc.approvalmicroservice.payload.Key;
import com.mitrais.cdc.approvalmicroservice.repository.BlogApprovalInProgressRepository;
import com.mitrais.cdc.approvalmicroservice.utility.UserContextHolder;
import io.reactivex.Single;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactiveApprovalServices {

    private BlogApprovalInProgressRepository blogApprovalInProgressRepository;
    private KafkaMessageServices kafkaMessageServices;
    private ApprovalService approvalService;

    public ReactiveApprovalServices(BlogApprovalInProgressRepository blogApprovalInProgressRepository, KafkaMessageServices kafkaMessageServices) {
        this.blogApprovalInProgressRepository = blogApprovalInProgressRepository;
        this.kafkaMessageServices = kafkaMessageServices;
    }

    public Single<Page<BlogApprovalInProgress>> getAllApprovalBlogData(String approvalProgress, Pageable pageable){
        return Single.create(data -> data.onSuccess(blogApprovalInProgressRepository.findApprovalDataByProgress(approvalProgress, pageable)));
    }

    public Single<BlogApprovalInProgress> updateProgressStatus(Long id, String status, String progress){
        return Single.create(update -> {
            BlogApprovalInProgress blogApprovalInProgress = blogApprovalInProgressRepository.findById(id).get();
            if(status.equals("Approved")){
                blogApprovalInProgress.setStatus(true);
            }else {
                blogApprovalInProgress.setStatus(false);
            }
            blogApprovalInProgress.setApprovalProgress(progress);

            /*
             * Only send Update Status event to Message Broker
             * when status either approved or rejected with progress Done
             * **/

            if((status.equals("Approved") && progress.equals("Done")) || (status.equals("Rejected") && progress.equals("Done")) ){
                kafkaMessageServices.updateBlogStatus(blogApprovalInProgress);
            }

            kafkaMessageServices.sendKey(new Key(UserContextHolder.getContext().getAuthToken()));
            kafkaMessageServices.sendUpdateProgressNotification(blogApprovalInProgress);
            kafkaMessageServices.sendApprovalResultStatistic(approvalService.getApprovalResultStatisticV2());
            kafkaMessageServices.sendApprovalResultStatisticV2(approvalService.getApprovalResultStatistic());
            kafkaMessageServices.sendBlogApprovalEvent(approvalService.getApprovalStatistiV2());
            kafkaMessageServices.sendBlogApprovalV2Event(approvalService.getApprovalStatistic());

            update.onSuccess(blogApprovalInProgress);
        });
    }

    public Single<List<ApprovalNumberPerProgressResponse>> getApprovalStatistic(){

        return Single.create(approve -> {
            List<ApprovalNumberPerProgress> approvalNumberPerProgressList = blogApprovalInProgressRepository.getApprovalStatistic();
            List<ApprovalNumberPerProgressResponse> result = new ArrayList<>();
            long rownum = blogApprovalInProgressRepository.getApprovalNumber().getRownum();

            for(ApprovalNumberPerProgress approvalNumberPerProgress:approvalNumberPerProgressList){
                ApprovalNumberPerProgressResponse response = new ApprovalNumberPerProgressResponse();
                response.setLabel(approvalNumberPerProgress.getLabel());
                response.setY(((new Double(approvalNumberPerProgress.getY())/new Double(rownum))*100));
                result.add(response);
            }

            approve.onSuccess(result);
        });
    }

    public Single<List<ApprovalNumberPerProgress>> getApprovalStatistiV2(){
        return Single.create(approve -> approve.onSuccess(blogApprovalInProgressRepository.getApprovalStatistic()));
    }

    public Single<Long> getApprovalNumber(){
        return Single.create(approve -> {
            approve.onSuccess(blogApprovalInProgressRepository.getApprovalNumber().getRownum());
        });
    }

    public Single<List<ApprovalNumberPerProgressResponse>> getApprovalResultStatistic(){

        return Single.create(approve -> {
            List<ApprovalResultStatistic> approvalResultList = blogApprovalInProgressRepository.getApprovalResultStatistic("Done");
            List<ApprovalNumberPerProgressResponse> results = new ArrayList<>();
            long resultnum = blogApprovalInProgressRepository.getApprovalNumber().getRownum();

            for(ApprovalResultStatistic approvalNumberPerProgress:approvalResultList){
                ApprovalNumberPerProgressResponse response = new ApprovalNumberPerProgressResponse();
                response.setLabel(approvalNumberPerProgress.getLabel());
                response.setY(((new Double(approvalNumberPerProgress.getY())/new Double(resultnum))*100));
                results.add(response);
            }

            approve.onSuccess(results);
        });
    }

    public Single<List<ApprovalResultStatistic>> getApprovalResultStatisticV2(){
        return Single.create(approve -> {
            approve.onSuccess(blogApprovalInProgressRepository.getApprovalResultStatistic("Done"));
        });
    }
}
