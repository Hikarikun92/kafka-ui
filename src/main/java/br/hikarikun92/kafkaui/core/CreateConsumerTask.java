package br.hikarikun92.kafkaui.core;

import br.hikarikun92.kafkaui.ui.ConsumerUi;
import br.hikarikun92.kafkaui.xml.ConsumerConfiguration;
import br.hikarikun92.kafkaui.xml.Property;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class designed to configure and create a {@link KafkaConsumer} in another thread.
 */
public class CreateConsumerTask implements Runnable {
    private final ConsumerConfiguration configuration;
    private final ConsumerUi ui;

    /**
     * Create an instance of this class on a {@link ConsumerConfiguration} and a {@link ConsumerUi}. The configuration
     * should be pre-processed before in a way that all the required properties (both common and specific) are set, and
     * the UI has already been set up ({@link ConsumerUi#setup()}.
     *
     * @param configuration An instance of a configuration object for a given consumer.
     * @param ui            The UI the consumer should interact with.
     */
    public CreateConsumerTask(ConsumerConfiguration configuration, ConsumerUi ui) {
        this.configuration = configuration;
        this.ui = ui;
    }

    @Override
    public void run() {
        try (KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(getPropertiesAsMap())) {
            consumer.subscribe(configuration.getTopics());

            new CoreConsumer(consumer, ui).run();
        }
    }

    /**
     * Convert the properties of the consumer to a {@link Map} to be used by the {@link KafkaConsumer}.
     *
     * @return A map with the defined properties.
     */
    private Map<String, Object> getPropertiesAsMap() {
        final List<Property> properties = configuration.getProperties();

        final Map<String, Object> propertyMap = new HashMap<>(properties.size());
        for (Property property : properties) {
            propertyMap.put(property.getName(), property.getValue());
        }

        return propertyMap;
    }
}
