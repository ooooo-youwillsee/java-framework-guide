package com.ooooo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * only test for pulsar
 * <p></p>
 * <a href="https://pulsar.apache.org/docs/en/client-libraries-java/">Usage for java</a>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/10/10 09:57
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class PulsarTestBootstrap {

	private static final String TEST_TOPIC = "test";

	private static final String TEST_TOPIC_GROUP = "test_group";

	@Autowired
	private PulsarClient pulsarClient;

	/**
	 * pulsar producer send message
	 */
	@Bean
	public ApplicationRunner producerSendMessage() {
		return __ -> {
			Producer<byte[]> producer = pulsarClient.newProducer().topic(TEST_TOPIC).create();

			for (int i = 0; i < 100; i++) {

				String key = "test key" + i;
				String message = "test message" + i;
				String propertyKey = "propertyKey" + i;
				String propertyValue = "propertyValue" + i;

				log.info("send message, key: {}, message: {}, propertyKey: {}, propertyValue: {}", key, message, propertyKey, propertyValue);

				producer.newMessage()
								.key(key)
								.property(propertyKey, propertyValue)
								.value(message.getBytes(StandardCharsets.UTF_8))
								.send()
				;
			}
		};
	}

	/**
	 * pulsar consumer subscribe message
	 */
	@Bean
	public ApplicationRunner consumerSubscribeMessage() {
		return __ -> {

			// message listener
			MessageListener<byte[]> messageListener = (consumer, msg) -> {
				try {
					// Do something with the message
					log.info("Message received: " + new String(msg.getData()));

					// Acknowledge the message so that it can be deleted by the message broker
					consumer.acknowledge(msg);
				} catch (Exception e) {
					// Message failed to process, redeliver later
					consumer.negativeAcknowledge(msg);
				}
			};

			pulsarClient.newConsumer()
							.topic(TEST_TOPIC)
							.subscriptionName(TEST_TOPIC_GROUP)
							.messageListener(messageListener)
							.subscribe()
			;
		};
	}
}
