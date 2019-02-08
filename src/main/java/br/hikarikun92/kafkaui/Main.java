package br.hikarikun92.kafkaui;

import br.hikarikun92.kafkaui.core.CreateConsumerTask;
import br.hikarikun92.kafkaui.ui.ConsumerUi;
import br.hikarikun92.kafkaui.ui.HeadlessConsumerUi;
import br.hikarikun92.kafkaui.xml.ConfigurationProcessor;
import br.hikarikun92.kafkaui.xml.ConsumerConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //Open the file name passed as argument, or "config.xml" if none
        final String fileName = (args.length > 0) ? args[0] : "config.xml";
        final ConfigurationProcessor processor = new ConfigurationProcessor(new FileReader(new File(fileName)));

        //Use command-line for now
        final ConsumerUi ui = new HeadlessConsumerUi();
        ui.setup();

        //Create and start each consumer in a separate thread, each of them coordinated by the same UI instance
        for (ConsumerConfiguration consumerConfiguration : processor.getConsumerConfigurations()) {
            new Thread(new CreateConsumerTask(consumerConfiguration, ui)).start();
        }
    }
}
