# Apache Kafka & Spring support

## What is Kafka?

Apache Kafka is an open-source distributed event streaming platform used by thousands of companies for high-performance data pipelines, streaming analytics, data integration, and mission-critical applications.

Spring Kafka brings the simple and typical Spring template programming model with a KafkaTemplate and Message-driven POJOs (Plain Old Java Object) via @KafkaListener annotation.

Kafka stores key-value messages that come from arbitrarily many processes called producers. The data can be partitioned into different "partitions" within different "topics". Within a partition, messages are strictly ordered by their offsets (the position of a message within a partition), and indexed and stored together with a timestamp. Other processes called "consumers" can read messages from partitions. For stream processing, Kafka offers the Streams API that allows writing Java applications that consume data from Kafka and write results back to Kafka.

## Setup

To setup your project to use kafka, you need to add Kafka to your dependencies in your ```build.gradle``` file as follows:

```gradle
implementation 'org.springframework.kafka:spring-kafka'
```

## Producing messages

To create messages, we first need to configure a ProducerFactory. This sets the strategy for creating Kafka Producer instances.

Then we need a KafkaTemplate, which wraps a Producer instance and provides convenience methods for sending messages to Kafka topics.

Producer instances are thread safe. So, using a single instance throughout an application context will give higher performance. Consequently, KakfaTemplate instances are also thread safe, and use of one instance is recommended.

### Configuration

```java
@Configuration
public class KafkaMessagingConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private List<String> bootstrapAddress;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NotificationSenderService notificationSenderService(final ObjectMapper objectMapper,
                                                               final KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaNotificationSenderService(objectMapper, kafkaTemplate);
    }
}
```

### Publishing messages

We can send messages using the KafkaTemplate class:

```java
private final KafkaTemplate<String, String> kafkaTemplate;

@Override
public <T> void sendNotification(String topic, T message) {
    var messageAsString = "";
    try {
        messageAsString = objectMapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
        return;
    }

    kafkaTemplate.send(topic, messageAsString);
}
```


# Consuming messages

### Consumer configuration

For consuming messages, we need to configure a ConsumerFactory and a KafkaListenerContainerFactory. Once these beans are available in the Spring bean factory, POJO-based consumers can be configured using `@KafkaListener` annotation.

`@EnableKafka` annotation is required on the configuration class to enable the detection of @KafkaListener annotation on spring-managed beans:

```java
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private List<String> bootstrapAddress;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        final Map<String, Object> props = new HashMap<>();
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

### Consuming Messages

Since in the current application we are using Kafka to send notification emails, this is how the implementation looks:

```java
 @KafkaListener(topics = "${app.messaging.topics.notifications.EMAIL}")
public void receiveMessage(String message) {
    NotificationProcessorReceiverDto input;
    try {
        input = objectMapper.readValue(message, NotificationProcessorReceiverDto.class);
    } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
        return;
    }

    final var parsedContent = emailTemplateParser.parseTemplate(input.getNotificationType(), input);
    if(parsedContent.isEmpty()) {
        log.error("Content count not be parsed. No email will be sent.");
        return;
    }

    emailSenderService.sendEmail(
            parsedContent.get().getTo(),
            parsedContent.get().getSubject(),
            parsedContent.get().getContent()
    );
}
```

### Set Mapping Types in the producer

```java
@Bean
public ConsumerFactory<String, String> consumerFactory() {
    final Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "ubbsoftware");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(props);
}
```

