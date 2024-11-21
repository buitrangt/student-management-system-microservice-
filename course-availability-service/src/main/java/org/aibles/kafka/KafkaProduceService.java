package org.aibles.kafka;

public interface KafkaProduceService {

    /**
     * function sends data to topic
     * @param message - data content
     * @param topic - topic for kafka
     */
    void pushMessage(String message, String topic);
}
