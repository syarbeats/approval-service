package com.mitrais.cdc.approvalmicroservice.controller;

import com.mitrais.cdc.approvalmicroservice.entity.BlogApprovalInProgress;
import com.mitrais.cdc.approvalmicroservice.services.ApprovalService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class ApprovalController extends CrossOriginController{

    private ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/approval-list")
    public ResponseEntity<List<BlogApprovalInProgress>> getAllBlogApprovalData(@RequestParam("approval") String approvalProgess, Pageable pageable){
        return ResponseEntity.ok(approvalService.getAllApprovalBlogData(approvalProgess,pageable).getContent());
    }

    @PostMapping("/process")
    public ResponseEntity<BlogApprovalInProgress> updateProgressStatus(@RequestParam("id") int id, @RequestParam("status") String status, @RequestParam("progress") String progress){
        return ResponseEntity.ok(approvalService.updateProgressStatus(id, status, progress));
    }
}
