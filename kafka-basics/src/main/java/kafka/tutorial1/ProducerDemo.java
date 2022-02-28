package kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        //create Producer properties
        String username="root";
        String password="EvaiKiO1";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
       /* //properties.setProperty("ssl.enabled.protocols","TLSv1.2,TLSv1.1,TLSv1");
        //properties.setProperty("ssl.truststore.location","truststore.jks");
       // properties.setProperty("ssl.truststore.password","");
      //  properties.setProperty("ssl.protocol","TLS");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "PLAIN");
        final String value = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""+username+"\" password=\""+password+"\";";
        properties.put("sasl.jaas.config", value);
        properties.put("retries", 1); // retries on transient errors and load balancing disconnection
        properties.put("max.request.size", 1024 * 1024); // limit request size to 1MB*/



        // create producer
        KafkaProducer<String,String> producer=new KafkaProducer<String, String>(properties);

        //create a producer record
        ProducerRecord<String,String> record=new ProducerRecord<>("first-topic","Tu phir aa gya ?");

        //send data
        producer.send(record);

        //flush data
        producer.flush();
        producer.close();

    }

   /* public static void listFolderStructure(String username, String password,
                                           String host, int port, long defaultTimeoutSeconds, String command) throws IOException {

        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        try (ClientSession session = client.connect(username, host, port)
                .verify(defaultTimeoutSeconds, TimeUnit.SECONDS).getSession()) {
            session.addPasswordIdentity(password);
            session.auth().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);

            try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                 ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)) {
                channel.setOut(responseStream);
                try {
                    channel.open().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
                    try (OutputStream pipedIn = channel.getInvertedIn()) {
                        pipedIn.write(command.getBytes());
                        pipedIn.flush();
                    }

                    channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                            TimeUnit.SECONDS.toMillis(defaultTimeoutSeconds));
                    String responseString = new String(responseStream.toByteArray());
                    System.out.println(responseString);
                } finally {
                    channel.close(false);
                }
            }
        } finally {
            client.stop();
        }
    }
*/}
