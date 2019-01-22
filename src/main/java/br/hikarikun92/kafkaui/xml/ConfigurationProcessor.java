package br.hikarikun92.kafkaui.xml;

import javax.xml.bind.JAXB;
import java.io.Reader;
import java.util.*;

/**
 * Class designed to help pre-process the configurations in an XML input. Based on it, it adds the common properties to
 * the consumers' specific properties and collects all the topics subscribed by them in a single Set. The common
 * properties are added before the consumer's properties; thus, when iterated, the specific ones will come last; in case
 * the properties are converted to a {@link Map} and the consumer repeats one of the properties with a different value,
 * the consumer's properties will overwrite the common ones.
 */
public class ConfigurationProcessor {
    private final List<ConsumerConfiguration> consumerConfigurations;
    private final Set<String> allTopics;

    /**
     * Read and validate an XML input. The given XML can contain elements {@code commonProperties} and {@code consumers}
     * as lists, and each element inside {@code consumers} must contain a non-empty element {@code topics}. If
     * {@code commonProperties} and {@code consumers} are missing, they are interpreted as empty lists.
     *
     * @param reader The reader to parse. Can be either a file reader, an input stream reader, a String reader etc.
     */
    public ConfigurationProcessor(Reader reader) {
        final Configuration configuration = JAXB.unmarshal(reader, Configuration.class);

        final List<Property> commonProperties = safeList(configuration.getCommonProperties());

        this.consumerConfigurations = safeList(configuration.getConsumerConfigurations());
        allTopics = new HashSet<>();

        for (ConsumerConfiguration consumerConfiguration : this.consumerConfigurations) {
            final Set<String> topics = consumerConfiguration.getTopics();
            if (topics == null || topics.isEmpty()) {
                throw new IllegalArgumentException("Required element \"topics\" missing or empty");
            }

            final List<Property> allConsumerProperties = new ArrayList<>(commonProperties);
            final List<Property> consumerProperties = consumerConfiguration.getProperties();
            if (consumerProperties != null) {
                allConsumerProperties.addAll(consumerProperties);
            }
            consumerConfiguration.setProperties(allConsumerProperties);

            allTopics.addAll(topics);
        }
    }

    /**
     * If the list passed as parameter to this method is not {@code null}, this method returns it. Otherwise, an empty
     * list is returned to avoid a {@code null} value.
     *
     * @param list The list to check and possibly return.
     * @param <T>  The type of the list.
     * @return The list itself if it is not {@code null}, or an empty list otherwise.
     */
    private <T> List<T> safeList(List<T> list) {
        return (list == null) ? Collections.emptyList() : list;
    }

    /**
     * Retrieve the list of consumer configurations the XML contained.
     *
     * @return The list of consumer configurations.
     */
    public List<ConsumerConfiguration> getConsumerConfigurations() {
        return consumerConfigurations;
    }

    /**
     * Retrieve a set of all the topics the consumers should subscribe to.
     *
     * @return The set of all topics.
     */
    public Set<String> getAllTopics() {
        return allTopics;
    }
}
