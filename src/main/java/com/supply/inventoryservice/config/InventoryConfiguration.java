package com.supply.inventoryservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class InventoryConfiguration {

    @Value("${spring.kafka.topic.name.topic1}")
    private String topicOrderName;

    @Bean
    public NewTopic OrderTopic(){
        return TopicBuilder.name(topicOrderName)
                .build();
    }

    @Value("${spring.kafka.topic.name.topic2}")
    private String topicOrderAvailability;

    @Bean
    public NewTopic OrderAvailabilityTopic(){
        return TopicBuilder.name(topicOrderAvailability)
                .build();
    }

    @Value("${spring.kafka.topic.name.topic3}")
    private String topicSupplierOrder;

    @Bean
    public NewTopic OrderSupplierTopic(){
        return TopicBuilder.name(topicSupplierOrder)
                .build();
    }


    @Value("${spring.kafka.topic.name.topic4}")
    private String topicStockUpdate;

    @Bean
    public NewTopic StockOrderTopic(){
        return TopicBuilder.name(topicStockUpdate)
                .build();
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Ignore unknown properties during deserialization
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
