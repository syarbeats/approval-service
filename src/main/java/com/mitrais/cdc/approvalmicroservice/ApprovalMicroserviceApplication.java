package com.mitrais.cdc.approvalmicroservice;

import com.mitrais.cdc.approvalmicroservice.utility.KafkaCustomChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableEurekaClient
@EnableBinding(KafkaCustomChannel.class)
public class ApprovalMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApprovalMicroserviceApplication.class, args);
    }

}
