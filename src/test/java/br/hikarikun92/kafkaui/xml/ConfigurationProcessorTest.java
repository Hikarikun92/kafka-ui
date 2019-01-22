package br.hikarikun92.kafkaui.xml;

import org.junit.Test;

import javax.xml.bind.DataBindingException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationProcessorTest {
    private static final String TEST_FOLDER = "/ConfigurationProcessorTest/";

    @Test(expected = DataBindingException.class)
    public void testXmlWithoutElements() {
        createProcessor("empty");
    }

    private ConfigurationProcessor createProcessor(String filename) {
        try (StringReader reader = new StringReader(readFromResource(filename))) {
            return new ConfigurationProcessor(reader);
        }
    }

    private String readFromResource(String filename) {
        try {
            final String pathAsString = TEST_FOLDER + filename + ".xml";
            final Path path = Paths.get(getClass().getResource(pathAsString).toURI());

            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Test
    public void testXmlWithNullConsumers() {
        final ConfigurationProcessor processor = createProcessor("nullConsumers");
        assertTrue(processor.getConsumerConfigurations().isEmpty());
        assertTrue(processor.getAllTopics().isEmpty());
    }

    @Test
    public void testXmlWithEmptyConsumers() {
        final ConfigurationProcessor processor = createProcessor("emptyConsumers");
        assertTrue(processor.getConsumerConfigurations().isEmpty());
        assertTrue(processor.getAllTopics().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumerWithNullTopics() {
        createProcessor("consumerWithNullTopics");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumerWithoutTopics() {
        createProcessor("consumerWithoutTopics");
    }

    @Test
    public void testConsumersWithoutCommonProperties() {
        final ConfigurationProcessor processor = createProcessor("consumersWithoutCommonProperties");

        final List<ConsumerConfiguration> consumerConfigurations = processor.getConsumerConfigurations();
        assertEquals(3, consumerConfigurations.size());

        final ConsumerConfiguration consumer1 = consumerConfigurations.get(0);
        assertEquals(Collections.singleton("Topic-1"), consumer1.getTopics());
        assertTrue(consumer1.getProperties().isEmpty());

        final ConsumerConfiguration consumer2 = consumerConfigurations.get(1);
        assertEquals(Collections.singleton("Topic-2"), consumer2.getTopics());
        assertTrue(consumer2.getProperties().isEmpty());

        final ConsumerConfiguration consumer3 = consumerConfigurations.get(2);
        assertEquals(toSet("Topic-3", "Topic-4"), consumer3.getTopics());

        final List<Property> consumer3Properties = consumer3.getProperties();
        assertEquals(2, consumer3Properties.size());

        final List<Property> expectedProperties = Arrays.asList(new Property("name-1", "value-1"), new Property("name-2", "value-2"));
        assertEquals(expectedProperties, consumer3Properties);

        assertEquals(toSet("Topic-1", "Topic-2", "Topic-3", "Topic-4"), processor.getAllTopics());
    }

    private Set<String> toSet(String... values) {
        final Set<String> set = new HashSet<>(values.length);
        Collections.addAll(set, values);
        return set;
    }

    @Test
    public void testFullConfiguration() {
        final ConfigurationProcessor processor = createProcessor("fullConfiguration");

        final List<ConsumerConfiguration> consumerConfigurations = processor.getConsumerConfigurations();
        assertEquals(3, consumerConfigurations.size());

        final List<Property> expectedCommonProperties = Arrays.asList(new Property("common-1", "value-common-1"), new Property("common-2", "value-common-2"));

        final ConsumerConfiguration consumer1 = consumerConfigurations.get(0);
        assertEquals(Collections.singleton("Topic-1"), consumer1.getTopics());
        assertEquals(expectedCommonProperties, consumer1.getProperties());

        final ConsumerConfiguration consumer2 = consumerConfigurations.get(1);
        assertEquals(Collections.singleton("Topic-2"), consumer2.getTopics());
        assertEquals(expectedCommonProperties, consumer2.getProperties());

        final ConsumerConfiguration consumer3 = consumerConfigurations.get(2);
        assertEquals(toSet("Topic-3", "Topic-4"), consumer3.getTopics());

        final List<Property> expectedProperties3 = Arrays.asList(new Property("common-1", "value-common-1"),
                new Property("common-2", "value-common-2"), new Property("name-1", "value-1"),
                new Property("name-2", "value-2"));

        assertEquals(expectedProperties3, consumer3.getProperties());
    }
}
