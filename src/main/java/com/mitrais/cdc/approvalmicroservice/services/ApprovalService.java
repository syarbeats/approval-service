package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.repository.BlogApprovalInProgressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public List<ApprovalNumberPerProgress> getApprovalStatistic(){
        return blogApprovalInProgressRepository.getApprovalStatistic();
    }
}
