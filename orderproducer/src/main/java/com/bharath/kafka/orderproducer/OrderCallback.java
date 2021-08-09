package com.bharath.kafka.orderproducer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class OrderCallback implements Callback {

	@Override
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		System.out.println(metadata.partition());
		System.out.println(metadata.offset());
		System.out.println("Message Sent Successfully");
		if(exception!=null) {
			exception.printStackTrace();
		}
	
	}

}
