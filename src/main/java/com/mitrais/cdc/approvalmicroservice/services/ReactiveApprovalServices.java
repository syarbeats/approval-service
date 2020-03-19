package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgressResponse;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalResultStatistic;
import com.mitrais.cdc.approvalmicroservice.repository.BlogApprovalInProgressRepository;
import io.reactivex.Single;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactiveApprovalServices {

    private BlogApprovalInProgressRepository blogApprovalInProgressRepository;

    public ReactiveApprovalServices(BlogApprovalInProgressRepository blogApprovalInProgressRepository) {
        this.blogApprovalInProgressRepository = blogApprovalInProgressRepository;
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
