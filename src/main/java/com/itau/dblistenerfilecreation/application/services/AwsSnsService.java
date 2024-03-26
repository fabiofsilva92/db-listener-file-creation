package com.itau.dblistenerfilecreation.application.services;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AwsSnsService {

    private final AmazonSNS sns;

    private final Topic dbFilesTopic;

    public AwsSnsService(AmazonSNS sns, @Qualifier("snsDbFilesTopic") Topic dbFilesTopic) {
        this.sns = sns;
        this.dbFilesTopic = dbFilesTopic;
    }

    public void publish(String message) {
        log.info("Publicando mensagem -> {}",message);
        sns.publish(dbFilesTopic.getTopicArn(), message);
        log.info("Mensagem publicada");
    }

}
