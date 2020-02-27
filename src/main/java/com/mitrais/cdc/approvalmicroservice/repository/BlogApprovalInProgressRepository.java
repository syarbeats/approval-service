package com.mitrais.cdc.approvalmicroservice.repository;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumber;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress;
import com.mitrais.cdc.approvalmicroservice.payload.ApprovalResultStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogApprovalInProgressRepository extends JpaRepository<BlogApprovalInProgress, Long> {

    @Query("SELECT b FROM BlogApprovalInProgress b WHERE b.approvalProgress = :approvalProgress")
    Page<BlogApprovalInProgress> findApprovalDataByProgress(String approvalProgress, Pageable pageable);

    @Query("SELECT case when count(b)> 0 then true else false end FROM BlogApprovalInProgress b WHERE b.title = :title")
    boolean isExistByTitle(String title);

    @Query("SELECT new com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumberPerProgress(count(p.approvalProgress), p.approvalProgress)  FROM BlogApprovalInProgress p GROUP BY p.approvalProgress")
    List<ApprovalNumberPerProgress> getApprovalStatistic();

    @Query("SELECT new com.mitrais.cdc.approvalmicroservice.payload.ApprovalNumber(count(p.approvalProgress))  FROM BlogApprovalInProgress p")
    ApprovalNumber getApprovalNumber();

    @Query("SELECT new com.mitrais.cdc.approvalmicroservice.payload.ApprovalResultStatistic(count(p.status),  case when p.status = true then 'Approved' else 'Rejected' end )  FROM BlogApprovalInProgress p GROUP BY p.status")
    List<ApprovalResultStatistic> getApprovalResultStatistic();

}
