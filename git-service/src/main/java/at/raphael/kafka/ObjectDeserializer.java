package at.raphael.kafka;

import at.raphael.entity.KafkaPackage;
import at.raphael.entity.KafkaResponse;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ObjectDeserializer extends ObjectMapperDeserializer<KafkaPackage> {
    public ObjectDeserializer() {
        super(KafkaPackage.class);
    }
}
