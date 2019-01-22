package br.hikarikun92.kafkaui.ui;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * Interface with the basic methods a UI should implement to allow the consumer to interact with it.
 */
public interface ConsumerUi {
    /**
     * Create the UI and set up the components to display the Kafka messages in it.
     */
    void setup();

    /**
     * Process the received messages and add them to the UI.
     *
     * @param records The messages to be added to the UI.
     */
    void handleUpdate(ConsumerRecords<?, ?> records);

    /**
     * Check if the Kafka Consumer should keep consuming messages based on the status of the UI. This value can change,
     * for example, if a window previously created was closed.
     *
     * @return {@code true} if the Kafka Consumer should keep consuming messages; {@code false} otherwise.
     */
    boolean shouldContinue();
}
