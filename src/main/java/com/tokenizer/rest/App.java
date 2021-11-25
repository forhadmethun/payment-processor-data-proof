package com.tokenizer.rest;

import com.tokenizer.rest.config.AppConfiguration;
import com.tokenizer.rest.consumer.CardInfoConsumer;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Properties;

import static com.tokenizer.rest.util.Constants.*;


public class App extends Application<AppConfiguration> {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	public static final Properties consumerProperties = new Properties();

	@Override
	public void run(AppConfiguration c, Environment e) throws Exception
	{
		configureRedis(c);
		configureConsumerProperties(c);
		CardInfoConsumer.consume();
	}

	private void configureConsumerProperties(AppConfiguration c) {
		consumerProperties.put(BOOTSTRAP_SERVER, c.getBootstrapServer());
        consumerProperties.put(KEY_DESERIALIZER, c.getKeyDeserializer());
        consumerProperties.put(VALUE_DESERIALIZER, c.getValueDeserializer());
        consumerProperties.put(TOPIC, c.getTopicEncryptedCardInfo());
        consumerProperties.put(GROUP_ID, c.getGroupId());
		consumerProperties.put(ENCRYPTION_KEY, c.getEncryptionKey());
	}

	private void configureRedis(AppConfiguration c) {
		try (Jedis jedis = new Jedis(c.getRedisHost(), Integer.parseInt(c.getRedisPort()))) {
			c.setEncryptionKey(jedis.get(ENCRYPTION_KEY));
		}catch (Exception e) {
			logger.error("Failed to connect redis");
		}
	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}
