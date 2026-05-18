package com.twitter.model.tweet;
import com.twitter.model.user.Usuario;
import java.util.List;


public class TweetSimple extends Tweet {

    public TweetSimple(Usuario autor, String contenido, List<String> hashtags) {
        super(autor, contenido, hashtags);
    }

    public TweetSimple(Usuario autor, String contenido) {
        super(autor, contenido, null);
    }

    @Override
    public String getTipo() {
        return "TweetSimple";
    }
}