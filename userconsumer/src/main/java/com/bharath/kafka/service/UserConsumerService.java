package com.bharath.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bharath.kafka.dto.User;

@Service
public class UserConsumerService {

	@KafkaListener(topics = { "user-topic" })
	public void consumerUserData(User user) {
		System.out.println("Users Age Is: " + user.getAge()+" Fav Genre "+user.getFavGenre());
	}
}
