package in.gov.abdm.nmr.config;
import java.util.HashMap;
import java.util.Map;
import in.gov.abdm.event.AuditLogEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {
    @Value ("${spring.kafka.bootstrap-servers}")
    String kafkaServer;
    @Bean
    public ProducerFactory<String, AuditLogEvent> producerFactory()
    {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate kafkaTemplate()
    {
        return new KafkaTemplate<>(producerFactory());
    }
}
