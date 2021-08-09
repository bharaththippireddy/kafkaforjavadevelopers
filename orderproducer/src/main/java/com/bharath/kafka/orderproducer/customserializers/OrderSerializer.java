package com.bharath.kafka.orderproducer.customserializers;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderSerializer implements Serializer<Order> {

	@Override
	public byte[] serialize(String topic, Order order) {
		byte[] response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			response = objectMapper.writeValueAsString(order).getBytes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}

}
