package kafka.tutorial2;

import com.fasterxml.jackson.databind.ser.BeanSerializer;
import lombok.Getter;
import lombok.Setter;
import twitter4j.Status;

import java.io.Serializable;
@Getter
@Setter
public class Tweet implements Serializable {
    private long id;
    private String text;
    private String source;
    private boolean isRetweeted;
    private int retweetCount;
    private String lang;
    private static final long serialVersionUID = 1L;

    public Tweet(Status status) {
        this.id = status.getId();
        this.text = status.getText();
        this.source = status.getSource();
        this.isRetweeted = status.isRetweeted();
        this.retweetCount = status.getRetweetCount();
        this.lang = status.getLang();
    }
}
