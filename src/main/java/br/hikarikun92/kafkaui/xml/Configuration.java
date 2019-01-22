package br.hikarikun92.kafkaui.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Base class for the configurations that should be used in this application. Based on an instance of this class, the
 * application should be able to configure and start a number of Kafka consumers, each of them with its own list of
 * properties and subscribed to one or more topics.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Configuration {
    private List<Property> commonProperties;
    private List<ConsumerConfiguration> consumerConfigurations;

    /**
     * Retrieve the list of properties that should be assigned to all consumers.
     *
     * @return The properties common to all consumers.
     */
    @XmlElementWrapper
    @XmlElement(name = "property")
    public List<Property> getCommonProperties() {
        return commonProperties;
    }

    /**
     * Set the properties that should be assigned to all consumers.
     *
     * @param commonProperties The value to set.
     */
    public void setCommonProperties(List<Property> commonProperties) {
        this.commonProperties = commonProperties;
    }

    /**
     * Retrieve the list of configurations for specific consumers.
     *
     * @return The list of those configurations.
     */
    @XmlElementWrapper(name = "consumers")
    @XmlElement(name = "consumer")
    public List<ConsumerConfiguration> getConsumerConfigurations() {
        return consumerConfigurations;
    }

    /**
     * Set the configurations used to create specific consumers.
     *
     * @param consumerConfigurations The value to set.
     */
    public void setConsumerConfigurations(List<ConsumerConfiguration> consumerConfigurations) {
        this.consumerConfigurations = consumerConfigurations;
    }
}
