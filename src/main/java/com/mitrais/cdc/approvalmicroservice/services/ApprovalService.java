package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumber;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgressResponse;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalResultStatistic;
import com.mitrais.cdc.approvalmicroservice.repository.BlogApprovalInProgressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {

    private BlogApprovalInProgressRepository blogApprovalInProgressRepository;

    public ApprovalService(BlogApprovalInProgressRepository blogApprovalInProgressRepository) {
        this.blogApprovalInProgressRepository = blogApprovalInProgressRepository;
    }

    public Page<BlogApprovalInProgress> getAllApprovalBlogData(String approvalProgress,Pageable pageable){
        /*return blogApprovalInProgressRepository.findAll(pageable);*/
        return  blogApprovalInProgressRepository.findApprovalDataByProgress(approvalProgress, pageable);
    }

    public BlogApprovalInProgress updateProgressStatus(Long id, String status, String progress){
        BlogApprovalInProgress blogApprovalInProgress = blogApprovalInProgressRepository.findById(id).get();

        if(status.equals("Approved")){
            blogApprovalInProgress.setStatus(true);
        }else {
            blogApprovalInProgress.setStatus(false);
        }

        blogApprovalInProgress.setApprovalProgress(progress);
        return blogApprovalInProgressRepository.save(blogApprovalInProgress) ;
    }

    public List<ApprovalNumberPerProgressResponse> getApprovalStatistic(){

        List<ApprovalNumberPerProgress> approvalNumberPerProgressList = blogApprovalInProgressRepository.getApprovalStatistic();
        List<ApprovalNumberPerProgressResponse> result = new ArrayList<>();
        long rownum = blogApprovalInProgressRepository.getApprovalNumber().getRownum();

        for(ApprovalNumberPerProgress approvalNumberPerProgress:approvalNumberPerProgressList){
            ApprovalNumberPerProgressResponse response = new ApprovalNumberPerProgressResponse();
            response.setLabel(approvalNumberPerProgress.getLabel());
            response.setY(((new Double(approvalNumberPerProgress.getY())/new Double(rownum))*100));
            result.add(response);
        }

        return result;
    }

    public List<ApprovalNumberPerProgress> getApprovalStatistiV2(){
        return blogApprovalInProgressRepository.getApprovalStatistic();
    }

    public long getApprovalNumber(){
        return blogApprovalInProgressRepository.getApprovalNumber().getRownum();
    }

    public List<ApprovalNumberPerProgressResponse> getApprovalResultStatistic(){

        List<ApprovalResultStatistic> approvalResultList = blogApprovalInProgressRepository.getApprovalResultStatistic();
        List<ApprovalNumberPerProgressResponse> results = new ArrayList<>();
        long resultnum = blogApprovalInProgressRepository.getApprovalNumber().getRownum();

        for(ApprovalResultStatistic approvalNumberPerProgress:approvalResultList){
            ApprovalNumberPerProgressResponse response = new ApprovalNumberPerProgressResponse();
            response.setLabel(approvalNumberPerProgress.getLabel());
            response.setY(((new Double(approvalNumberPerProgress.getY())/new Double(resultnum))*100));
            results.add(response);
        }

        return results;
    }

    public List<ApprovalResultStatistic> getApprovalResultStatisticV2(){
        return blogApprovalInProgressRepository.getApprovalResultStatistic();
    }
}
