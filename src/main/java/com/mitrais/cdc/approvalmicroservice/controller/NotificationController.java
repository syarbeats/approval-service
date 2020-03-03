package com.mitrais.cdc.approvalmicroservice.controller;

import com.mitrais.cdc.approvalmicroservice.payload.*;
import com.mitrais.cdc.approvalmicroservice.services.NotificationServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class NotificationController extends CrossOriginController{

    private NotificationServices notificationServices;

    public NotificationController(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }

    @RequestMapping("/send/{topic}")
    public String sender(@PathVariable String topic, @RequestParam String message){
        notificationServices.sendMessageTo(topic, message);
        return "OK-Sent";
    }

    @PostMapping("/update-chart")
    public String updateChart(@RequestBody List<BlogStatistic> blogStatisticList){
        log.info("Receive object:"+blogStatisticList.get(0).getLabel());
        notificationServices.sendObject(blogStatisticList, "percentage");
        return "OK-Sent";
    }

    @PostMapping("/update-chart-v2")
    public String updateChartV2(@RequestBody List<BlogNumberPerCategory> blogNumberPerCategoryList){
        log.info("Receive object:"+blogNumberPerCategoryList.get(0).getLabel());
        notificationServices.sendBlogAmountPerCategoryMessage(blogNumberPerCategoryList, "amount");
        return "OK-Sent";
    }

    @PostMapping("/update-approval-chart")
    public String updateApprovalChart(@RequestBody List<ApprovalNumberPerProgress> approvalNumberPerProgressList){
        log.info("Receive object:"+approvalNumberPerProgressList.get(0).getLabel());
        notificationServices.sendBlogApprovalStatistic(approvalNumberPerProgressList);
        return "OK-Sent";
    }

    @PostMapping("/update-approval-result-chart")
    public String updateApprovalResultChart(@RequestBody List<ApprovalResultStatistic> approvalResultStatistics){
        log.info("Receive object:"+approvalResultStatistics.get(0).getLabel());
        notificationServices.sendApprovalResultStatistic(approvalResultStatistics);
        return "OK-Sent";
    }

    @PostMapping("/update-approval-statistic-v2-chart")
    public String updateApprovalChartV2(@RequestBody List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses){
        log.info("Receive object:"+approvalNumberPerProgressResponses.get(0).getLabel());
        notificationServices.sendBlogApprovalStatisticv2(approvalNumberPerProgressResponses);
        return "OK-Sent";
    }

}
