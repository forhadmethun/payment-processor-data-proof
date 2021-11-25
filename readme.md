## Payment processor data source application

### Tech stack
- Java dropwizard
- Kafka
- Redis

### Running the project 
##### Run project
``` 
mvn clean package
java -jar target/PaymentProcessorDataProof-1.0-SNAPSHOT.jar server config.yml 
```
##### Create / View kafka topic 
``` 
bin/kafka-console-consumer.sh --topic encrypted-card-info --from-beginning --bootstrap-server localhost:9092
```

### Sample log 
``` 
INFO  [2021-11-24 23:05:56,479] com.tokenizer.rest.consumer.CardInfoConsumer: AuthModel{cardNumber='1231234412', expirationDate='02/20', cardCVC='332', transactionId='0bdc4ef37eac4c8a935dacaf094a1de4'}
```
