package com.mitrais.cdc.approvalmicroservice.services;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
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

    public BlogApprovalInProgress updateProgressStatus(int id, String status){
        BlogApprovalInProgress blogApprovalInProgress = blogApprovalInProgressRepository.findById(id).get();
        blogApprovalInProgress.setApprovalProgress(status);

        return blogApprovalInProgressRepository.save(blogApprovalInProgress) ;
    }
}
