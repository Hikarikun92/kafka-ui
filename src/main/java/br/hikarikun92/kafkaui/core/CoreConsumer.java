package br.hikarikun92.kafkaui.core;

import br.hikarikun92.kafkaui.ui.ConsumerUi;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for managing a {@link Consumer} instance as well as a UI for the messages.
 */
public class CoreConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreConsumer.class);

    private final Consumer<?, ?> consumer;
    private final ConsumerUi ui;

    /**
     * Create an instance of this class with a given consumer and a {@link ConsumerUi} implementation.
     *
     * @param consumer The consumer to use.
     * @param ui       A UI implementation this consumer should interact with.
     */
    public CoreConsumer(Consumer<?, ?> consumer, ConsumerUi ui) {
        this.consumer = consumer;
        this.ui = ui;
    }

    /**
     * Run the consumer. This will set the consumer to the beginning of its topics and make it start consuming the
     * messages. The consumer will keep reading them as long as the UI instructs so.
     *
     * @see ConsumerUi#shouldContinue()
     */
    public void run() {
        //Set the consumer to the beginning of the topics (https://stackoverflow.com/a/45623878)
        //At this point, there is no heartbeat from consumer so seekToBeginning() won't work, so call poll()
        consumer.poll(0L);
        // Now there is heartbeat and consumer is "alive"
        consumer.seekToBeginning(consumer.assignment());
        // Now consume

        //We cannot close the consumer in another thread, as it is not thread-safe; it has to keep polling in this
        //thread and be closed normally when the poll loop ends.
        while (ui.shouldContinue()) {
            final ConsumerRecords<?, ?> records = consumer.poll(1000L);
            consumer.commitSync();
            ui.handleUpdate(records);
        }
    }
}
