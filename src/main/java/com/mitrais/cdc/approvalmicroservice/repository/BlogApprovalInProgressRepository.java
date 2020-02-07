package com.mitrais.cdc.approvalmicroservice.repository;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface BlogApprovalInProgressRepository extends JpaRepository<BlogApprovalInProgress, Serializable> {

    @Query("SELECT b FROM BlogApprovalInProgress b WHERE b.approvalProgress = :approvalProgress")
    Page<BlogApprovalInProgress> findApprovalDataByProgress(String approvalProgress, Pageable pageable);
}
