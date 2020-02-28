package com.mitrais.cdc.approvalmicroservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class BlogApprovalInProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String title;

    @Lob
    private String summary;
    private ZonedDateTime createdDate;
    private Long categoryId;
    private String categoryName;
    private boolean status;
    private String approvalProgress;

    public BlogApprovalInProgress(@NotNull String title, String summary, ZonedDateTime createdDate, Long categoryId, String categoryName, boolean status, String approvalProgress, String key) {
        this.title = title;
        this.summary = summary;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
        this.approvalProgress = approvalProgress;
    }

    public BlogApprovalInProgress() {

    }
}
