package kafka.tutorial2;

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class JavaRestTweet {
    static String consumerKeyStr = "GodgbrhWOce0SphpSzwPsd7IX";
    static String consumerSecretStr = "CrLyiGejEppk56lrzYAEpn41qbvhdwUGZvc7oA76DM8r51CrsF";
    static String accessTokenStr = "1224354823312695296-amlxq3KTL42EoeyKQcFsF9RwGvzqZg";
    static String accessTokenSecretStr = "KgJ0gcY59eOFSt4v0v9VRGRb6ZZ6GMmk3wRrVKKqVh4eT";


    public static void main(String[] args) {
        Twitter twitter = new TwitterFactory().getInstance();
        // Twitter Consumer key & Consumer Secret
        twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
        // Twitter Access token & Access token Secret
        twitter.setOAuthAccessToken(new AccessToken(accessTokenStr, accessTokenSecretStr));

        try {
            // Getting Twitter Timeline using Twitter4j API
            ResponseList<Status> statusResponseList = twitter.getUserTimeline(new Paging(1, 5));
            for (Status status : statusResponseList) {
                System.out.println(status.getText());
            }
            // Post a Tweet using Twitter4j API
            Status status = twitter.updateStatus("bjp");
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        } catch (Exception e) {
        }
    }
}
