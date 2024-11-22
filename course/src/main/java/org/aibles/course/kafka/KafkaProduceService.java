package org.aibles.course.kafka;


public interface KafkaProduceService {

    /**
     * Function to send a message to Kafka.
     *
     * @param topic   the Kafka topic
     * @param message the message to be sent
     */
    void pushMessage(String topic, String message);
}
