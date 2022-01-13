package com.unict.bookingorchestrator.configuration;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${KAFKA_ADDRESS}")
    private String bootstrapAddress;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String kafkaMainTopic;

    @Value(value = "${KAFKA_USER_REQUEST_TOPIC}")
    private String userRequestTopic;

    @Value(value = "${KAFKA_USER_RESPONSE_TOPIC}")
    private String userResponseTopic;

    @Value(value = "${KAFKA_HOTEL_REQUEST_TOPIC}")
    private String hotelRequestTopic;

    @Value(value = "${KAFKA_HOTEL_RESPONSE_TOPIC}")
    private String hotelResponseTopic;



    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(kafkaMainTopic, 100, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(userRequestTopic, 10, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic(userResponseTopic, 10, (short) 1);
    }

    @Bean
    public NewTopic topic4() {
        return new NewTopic(hotelRequestTopic, 10, (short) 1);
    }

    @Bean
    public NewTopic topic5() {
        return new NewTopic(hotelResponseTopic, 10, (short) 1);
    }



}
