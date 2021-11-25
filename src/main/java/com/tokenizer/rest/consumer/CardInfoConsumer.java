package com.tokenizer.rest.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokenizer.rest.crypto.Decryptor;
import com.tokenizer.rest.model.AuthModel;
import com.tokenizer.rest.model.EncryptedCardInfoModel;
import com.tokenizer.rest.request.AuthRequest;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;

import static com.tokenizer.rest.App.consumerProperties;
import static com.tokenizer.rest.util.Builders.build;
import static com.tokenizer.rest.util.Constants.ENCRYPTION_KEY;
import static com.tokenizer.rest.util.Constants.TOPIC;

public class CardInfoConsumer {

    private static Logger logger = LoggerFactory.getLogger(CardInfoConsumer.class);

    private CardInfoConsumer() {

    }

    public static void consume() {
        KafkaConsumer kafkaConsumer = new KafkaConsumer(consumerProperties);
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaConsumer.subscribe(Arrays.asList(consumerProperties.get(TOPIC)));

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            records.forEach(r -> {
                try {
                    EncryptedCardInfoModel encryptedCardInfoModel = objectMapper
                        .readValue(r.value(), EncryptedCardInfoModel.class);
                    AuthRequest authRequest = Decryptor.decrypt(encryptedCardInfoModel.getToken(),
                        (String) consumerProperties.get(ENCRYPTION_KEY));
                    AuthModel authModel = build(authRequest)
                        .setTransactionId(encryptedCardInfoModel.getTransactionId());
                    logger.info(authModel.toString());

                } catch (Exception e) {
                    logger.error("Failed to process request: " + r);
                }
            });
        }
    }

}
