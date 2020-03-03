package com.mitrais.cdc.approvalmicroservice.utility;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface KafkaCustomChannel {

    @Input("BlogCreationInput")
    SubscribableChannel blogCreationSubsChannel();

    @Output("BlogApprovalOutput")
    MessageChannel blogApprovalPubChannel();

    @Output("BlogApprovalNotificationOutput")
    MessageChannel blogApprovalNotificationPubChannel();

    @Output("BlogKeyOutput")
    MessageChannel blogPubKey();

    @Output("BlogNumberPerCategoryOutput")
    MessageChannel blogNumberPerCategoryPubChannel();

    @Output("BlogNumberPerCategoryOutputV2")
    MessageChannel blogNumberPerCategoryPubChannelV2();

    @Output("BlogApprovalStatisticOutput")
    MessageChannel blogApprovalStatisticOutput();

    @Output("BlogApprovalStatisticV2Output")
    MessageChannel blogApprovalStatisticV2Output();

    @Output("BlogApprovalResultStatisticOutput")
    MessageChannel blogApprovalResultStatisticOutput();

    @Output("BlogApprovalResultStatisticV2Output")
    MessageChannel blogApprovalResultStatisticV2Output();

}
