package com.bharath.kafka.simpleconsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

public class SimpleConsumer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "SimpleConsumerGroup");
		
		KafkaConsumer<String, Integer> consumer = new KafkaConsumer<>(props);
		List<PartitionInfo> partitionInfos = consumer.partitionsFor("SimpleConsumerTopic");
		
		ArrayList<TopicPartition> partitions = new ArrayList<>();
		
		for(PartitionInfo info:partitionInfos) {
			System.out.println(info.partition());
			partitions.add(new TopicPartition("SimpleConsumerTopic", info.partition()));
		}
		
		consumer.assign(partitions);	
		
		ConsumerRecords<String, Integer> orders = consumer.poll(Duration.ofSeconds(20));
		for (ConsumerRecord<String, Integer> order : orders) {
			System.out.println("Product Name " + order.key());
			System.out.println("Quantiy " + order.value());
		}
		consumer.commitAsync();
		consumer.close();
	}

}
