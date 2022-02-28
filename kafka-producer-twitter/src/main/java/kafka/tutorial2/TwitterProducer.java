package kafka.tutorial2;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TwitterProducer {
    public static Logger logger = LoggerFactory.getLogger(TwitterProducer.class);


    public TwitterProducer() {
    }

    public static void main(String[] args) throws TwitterException, InterruptedException {

        //create twitter client
        TwitterProducer twitterProducer = new TwitterProducer();

        BlockingQueue<String> msgQueue = twitterProducer.searchTweets("bitcoin");


        //create kafka producer
        KafkaProducer<String, String> producer = createKafkaProducer();

        while (true) {
            String msg = null;
            msg = msgQueue.poll(5, TimeUnit.SECONDS);
            if (msg != null) {
                logger.info(msg);
                producer.send(new ProducerRecord<>("twitter-tweets", null, msg), (recordMetadata, e) -> {
                    if (e != null) {
                        logger.error("something bad has happened", e);
                    }
                });
            }
        }
    }

    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create safe producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        //high throughput producer (at the expense of a bit of latency and cpu usage)
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); //32 KB bath size


        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        return producer;
    }

    private Twitter getTwitterInstance() {
        String consumerKey = "yEzs3usW0MMwQyKkYpRex2itF";
        String consumerSecret = "wi53wzCHBu4Jrs03hGiJHqFI5fNBfDmICTVV6NrqeOSMxHvISx";
        String token = "1224354823312695296-MrborNzenxvjX5ylqjtNaiajQ4BPrb";
        String secret = "Lu9PX254Xoqi8SVGJK2HGOCqEGOYtpC723WSwqI9Tk1Y4";

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(token)
                .setOAuthAccessTokenSecret(secret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }

    public String createTweet(String tweet) throws TwitterException {
        Twitter twitter = getTwitterInstance();
        Status status = twitter.updateStatus("creating baeldung API");
        return status.getText();
    }

    public List<String> getTimeLine() throws TwitterException {
        Twitter twitter = getTwitterInstance();
        List<String> trends = twitter.getAvailableTrends().stream().map(item -> item.toString()).collect(Collectors.toList());
        trends.forEach(System.out::println);
        return twitter.getHomeTimeline().stream()
                .map(item -> item.getText())
                .collect(Collectors.toList());
    }

    public String sendDirectMessage(String recipientName, String msg)
            throws TwitterException {
        Twitter twitter = getTwitterInstance();
        DirectMessage message = twitter.sendDirectMessage(recipientName, msg);
        return message.getText();
    }

    public BlockingQueue<String> searchTweets(String searchString) throws TwitterException, InterruptedException {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);
        Twitter twitter = getTwitterInstance();
        Query query = new Query(searchString);
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            String tweet = convertObject(status);
            if (tweet != null) {
                msgQueue.put(tweet);
            }
        }
        return msgQueue;
    }

    public String convertObject(Status status) {
       // Tweet tweet=new Tweet(status);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(status);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
