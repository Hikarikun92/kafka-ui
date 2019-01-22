package br.hikarikun92.kafkaui.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import java.util.Set;

/**
 * Configuration that should be assigned to a specific Kafka consumer.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ConsumerConfiguration {
    private Set<String> topics;
    private List<Property> properties;

    /**
     * Retrieve the names of the topics this consumer should subscribe to.
     *
     * @return The topic names.
     */
    @XmlElementWrapper
    @XmlElement(name = "topic")
    public Set<String> getTopics() {
        return topics;
    }

    /**
     * Set the names of the topics this consumer should subscribe to.
     *
     * @param topics The value to set.
     */
    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    /**
     * Retrieve the list of properties specific for this consumer. Note that it does not include the common properties
     * list; for that, the user should define {@link Configuration#setCommonProperties(List)}.
     *
     * @return The list of specific properties.
     */
    @XmlElementWrapper
    @XmlElement(name = "property")
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Set the properties this consumer should use, not including the common properties defined in
     * {@link Configuration#setCommonProperties(List)}.
     *
     * @param properties The value to set.
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
