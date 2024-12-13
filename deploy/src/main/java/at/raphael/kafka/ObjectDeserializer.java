package at.raphael.kafka;

import at.raphael.entity.KafkaResponse;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

import java.nio.charset.StandardCharsets;

public class ObjectDeserializer extends ObjectMapperDeserializer<KafkaResponse> {
    public ObjectDeserializer() {
        super(KafkaResponse.class);
    }

    @Override
    public KafkaResponse deserialize(String topic, byte[] data) {
        try {
            String json = new String(data, StandardCharsets.UTF_8);
            System.out.println("Received JSON: " + json);
            return super.deserialize(topic, data);
        } catch (Exception e) {
            System.err.println("Deserialization error: " + e.getMessage());
            throw e;
        }
    }

}