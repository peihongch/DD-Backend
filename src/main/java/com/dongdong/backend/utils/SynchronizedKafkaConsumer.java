package com.dongdong.backend.utils;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 同步的KafkaConsumer
 */
public class SynchronizedKafkaConsumer<K, V> extends KafkaConsumer<K, V> {

    public SynchronizedKafkaConsumer(Properties properties) {
        super(properties);
    }

    @Override
    public synchronized Map<String, List<PartitionInfo>> listTopics() {
        return super.listTopics();
    }

    @Override
    public synchronized void subscribe(Collection<String> topics) {
        super.subscribe(topics);
    }

    @Override
    public synchronized ConsumerRecords<K, V> poll(Duration timeout) {
        return super.poll(timeout);
    }
}
