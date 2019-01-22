package br.hikarikun92.kafkaui.ui;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class HeadlessConsumerUiTest {
    private HeadlessConsumerUi ui;

    @Before
    public void setup() {
        ui = new HeadlessConsumerUi();
    }

    @Test
    public void testSetup() {
        ui.setup();
        //Nothing to validate
    }

    @Test
    public void testHandleUpdate() {
        final ConsumerRecord<String, String> record1 = new ConsumerRecord<>("topic", 0, 0L, "key1", "value1");
        final ConsumerRecord<Object, Object> record2 = new ConsumerRecord<>("topic2", 0, 0L, 2, 3);
        final ConsumerRecord<Object, Object> record3 = new ConsumerRecord<>("topic2", 1, 0L, 3L, "value1");
        final ConsumerRecord<Object, Object> record4 = new ConsumerRecord<>("topic2", 1, 1L, 0d, 2f);
        final ConsumerRecord<Object, Object> record5 = new ConsumerRecord<>("topic3", 0, 0L, new BigDecimal("2.1"), new BigInteger("123"));
        final ConsumerRecord<String, String> record6 = new ConsumerRecord<>("topic3", 1, 0L, "key2", "value3");

        final List<ConsumerRecord<String, String>> records1 = Collections.singletonList(record1);
        final List<ConsumerRecord<Object, Object>> records2 = Collections.singletonList(record2);
        final List<ConsumerRecord<Object, Object>> records3 = Arrays.asList(record3, record4);
        final List<ConsumerRecord<Object, Object>> records4 = Collections.singletonList(record5);
        final List<ConsumerRecord<String, String>> records5 = Collections.singletonList(record6);

        final Map<TopicPartition, List<ConsumerRecord<String, String>>> map1 = new HashMap<>(2);
        map1.put(new TopicPartition("topic", 0), records1);
        map1.put(new TopicPartition("topic3", 1), records5);

        final Map<TopicPartition, List<ConsumerRecord<Object, Object>>> map2 = new HashMap<>(3);
        map2.put(new TopicPartition("topic2", 0), records2);
        map2.put(new TopicPartition("topic2", 1), records3);
        map2.put(new TopicPartition("topic3", 0), records4);

        ui.handleUpdate(new ConsumerRecords<>(map1));
        ui.handleUpdate(new ConsumerRecords<>(map2));

        //No action
    }

    @Test
    public void testShouldContinue() {
        assertTrue(ui.shouldContinue());
    }
}
