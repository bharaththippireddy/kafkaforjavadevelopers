package com.bharath.kafka.orderconsumer.customdeserializers;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class OrderConsumer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("key.deserializer", StringDeserializer.class.getName());
		props.setProperty("value.deserializer", OrderDeserializer.class.getName());
		props.setProperty("group.id", "OrderGroup");
		//props.setProperty("auto.commit.interval.ms", "2000");
		props.setProperty("auto.commit.offset", "false");

		Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
				
		KafkaConsumer<String, Order> consumer = new KafkaConsumer<>(props);
		
		class RebalanceHandler implements ConsumerRebalanceListener{

			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			consumer.commitSync(currentOffsets);
			}

			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				
			}
			
		}
		
		consumer.subscribe(Collections.singletonList("OrderPartitionedTopic"),new RebalanceHandler());
		
		try {
			while (true) {
				ConsumerRecords<String, Order> records = consumer.poll(Duration.ofSeconds(20));
				int count = 0;
				
				for (ConsumerRecord<String, Order> record : records) {
					String customerName = record.key();
					Order order = record.value();
					System.out.println("Customer Name: " + customerName);
					System.out.println("Product: " + order.getProduct());
					System.out.println("Quantity: " + order.getQuantity());
					System.out.println("Partion: " + record.partition());
					currentOffsets.put(new TopicPartition(record.topic(), 
							record.partition()), 
							new OffsetAndMetadata(record.offset()+1));
					
					if(count%10==0) {
					
					consumer.commitAsync(currentOffsets,
							new OffsetCommitCallback() {
						
						@Override
						public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
							if(exception != null) {
								System.out.println("Commit failed for offset"+offsets);
							}
						}
					});
					}
					
					count++;
				}
				
				
			}
		} finally {
			consumer.close();
		}
	}

}
