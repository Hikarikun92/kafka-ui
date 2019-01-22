package br.hikarikun92.kafkaui.ui;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * No-op implementation of {@link ConsumerUi}. This should be used, for example, if the underlying environment does not
 * support a GUI (a command-line interface, for example). In this implementation, all methods have no effect.
 */
public class HeadlessConsumerUi implements ConsumerUi {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadlessConsumerUi.class);

    @Override
    public void setup() {
        LOGGER.info("UI is disabled; logging to console only");
    }

    @Override
    public void handleUpdate(ConsumerRecords<?, ?> records) {
        for (ConsumerRecord<?, ?> record : records) {
            final Instant instant = Instant.ofEpochMilli(record.timestamp());

            LOGGER.info("topic = {}, key = {}, value = {}, partition = {}, offset = {}, {} = {}", record.topic(),
                    record.key(), record.value(), record.partition(), record.offset(), record.timestampType(), instant);
        }
    }

    /**
     * As there is no user interface to interact with, this method will return {@code true}, indicating that the
     * consumer should always keep receiving messages.
     *
     * @return Always {@code true}.
     */
    @Override
    public boolean shouldContinue() {
        return true;
    }
}
