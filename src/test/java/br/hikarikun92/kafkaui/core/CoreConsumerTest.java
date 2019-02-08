package br.hikarikun92.kafkaui.core;

import br.hikarikun92.kafkaui.ui.ConsumerUi;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CoreConsumerTest {
    @Mock
    private Consumer<Object, Object> consumer;

    @Mock
    private ConsumerUi ui;

    private CoreConsumer coreConsumer;

    private final TopicPartition partition1 = new TopicPartition("topic-1", 0);
    private final TopicPartition partition2 = new TopicPartition("topic-2", 0);

    @Before
    public void setup() {
        coreConsumer = new CoreConsumer(consumer, ui);

        final Set<TopicPartition> assignment = new HashSet<>(2);
        assignment.add(partition1);
        assignment.add(partition2);

        when(consumer.assignment()).thenReturn(assignment);
    }

    @Test
    public void testShouldNotContinue() {
        when(ui.shouldContinue()).thenReturn(false);

        coreConsumer.run();

        verify(consumer).poll(0L);
        verify(consumer).seekToBeginning(consumer.assignment());
        verify(consumer, never()).poll(1000L);
        verify(consumer, never()).commitSync();
        verify(ui, never()).handleUpdate(any());
    }

    @Test
    public void testConsumeTwice() {
        when(ui.shouldContinue()).thenReturn(true).thenReturn(true).thenReturn(false);

        final ConsumerRecords<Object, Object> records1 = records1();
        final ConsumerRecords<Object, Object> records2 = records2();

        when(consumer.poll(1000L)).thenReturn(records1).thenReturn(records2);

        coreConsumer.run();

        verify(consumer).poll(0L);
        verify(consumer).seekToBeginning(consumer.assignment());

        verify(consumer, times(2)).poll(1000L);
        verify(consumer, times(2)).commitSync();

        verify(ui).handleUpdate(records1);
        verify(ui).handleUpdate(records2);
    }

    private ConsumerRecords<Object, Object> records1() {
        final Map<TopicPartition, List<ConsumerRecord<Object, Object>>> recordsMap = new HashMap<>(2);

        final List<ConsumerRecord<Object, Object>> topic1Messages = new ArrayList<>(2);
        topic1Messages.add(new ConsumerRecord<>(partition1.topic(), partition1.partition(), 0L, "key1", "value1"));
        topic1Messages.add(new ConsumerRecord<>(partition1.topic(), partition1.partition(), 1L, 2, 3));

        recordsMap.put(partition1, topic1Messages);

        final List<ConsumerRecord<Object, Object>> topic2Messages = Collections.singletonList(new ConsumerRecord<>(partition2.topic(), partition2.partition(), 0L, new Object(), "value2"));

        recordsMap.put(partition2, topic2Messages);

        return new ConsumerRecords<>(recordsMap);
    }

    private ConsumerRecords<Object, Object> records2() {
        final Map<TopicPartition, List<ConsumerRecord<Object, Object>>> recordsMap = new HashMap<>(2);

        final List<ConsumerRecord<Object, Object>> topic1Messages = new ArrayList<>(3);
        topic1Messages.add(new ConsumerRecord<>(partition1.topic(), partition1.partition(), 0L, 3L, "value1"));
        topic1Messages.add(new ConsumerRecord<>(partition1.topic(), partition1.partition(), 1L, 0d, 2f));
        topic1Messages.add(new ConsumerRecord<>(partition1.topic(), partition1.partition(), 2L, new BigDecimal("2.1"), new BigInteger("123")));

        recordsMap.put(partition1, topic1Messages);

        final List<ConsumerRecord<Object, Object>> topic2Messages = Collections.singletonList(new ConsumerRecord<>(partition2.topic(), partition2.partition(), 0L, "key2", "value3"));

        recordsMap.put(partition2, topic2Messages);

        return new ConsumerRecords<>(recordsMap);
    }
}
