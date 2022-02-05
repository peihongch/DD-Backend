package com.dongdong.backend.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Value("spring.kafka.admin.client-id")
    private String clientID;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

//    @Value("${spring.kafka.consumer.group-id}")
//    private String kafkaGroupID = "";

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String kafkaKeyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String kafkaValueDeserializer;

    @Bean(name = "kafkaProps")
    public Properties kafkaProps() {
        Properties props = new Properties();
        // 配置信息
        // admin client id
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientID);
        // kafka服务器地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        // 必须指定消费者组
        // props.put("group.id", kafkaGroupID);
        // 设置数据key和value的序列化处理类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaKeyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaValueDeserializer);
        return props;
    }

    @Bean
    public AdminClient createAdminClient() {
        return AdminClient.create(kafkaProps());
    }

}
