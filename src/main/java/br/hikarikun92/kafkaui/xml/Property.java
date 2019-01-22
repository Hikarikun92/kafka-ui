package br.hikarikun92.kafkaui.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Objects;

/**
 * Class defining a name-value association according to the values in the XML configuration file. The name of this
 * property should be one recognized by Kafka and its associated classes, as well as their values. Some examples can be
 * seen in the String constants of the related classes mentioned here in "See also".
 *
 * @see org.apache.kafka.clients.CommonClientConfigs
 * @see org.apache.kafka.clients.consumer.ConsumerConfig
 * @see org.apache.kafka.clients.producer.ProducerConfig
 * @see io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
 * @see io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Property {
    private String name;
    private String value;

    /**
     * Create an empty instance of this class, with its fields set to {@code null}.
     */
    public Property() {
    }

    /**
     * Create an instance of this class with the fields set according to the parameters.
     *
     * @param name  The name of the property.
     * @param value The value of the property.
     */
    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Retrieve the name (key) of this property.
     *
     * @return The name of this property.
     */
    @XmlAttribute
    public String getName() {
        return name;
    }

    /**
     * Set the name of this property. This name should be recognized by Kafka and its associated classes, as it will be
     * used as a key in the client's properties map.
     *
     * @param name The name of this property.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the value of this property that will be used by the Kafka clients.
     *
     * @return The value of this property.
     */
    @XmlAttribute
    public String getValue() {
        return value;
    }

    /**
     * Set the value of this property. This value will be used by the Kafka clients in their configurations.
     *
     * @param value The value of this property.
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;
        return Objects.equals(name, property.name) &&
                Objects.equals(value, property.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
