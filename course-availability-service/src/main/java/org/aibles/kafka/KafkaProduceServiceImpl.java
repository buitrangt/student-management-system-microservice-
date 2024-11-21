package org.aibles.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProduceServiceImpl implements KafkaProduceService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void pushMessage(String topic, String message) {
        try{
            CompletableFuture.runAsync(() -> {
                kafkaTemplate.send(topic,message);
            });
        } catch (Throwable ex) {
            log.info("Exception is: " + ex);
        }
    }
}
