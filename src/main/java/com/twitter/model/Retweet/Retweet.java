package com.twitter.model.Retweet;

import com.twitter.model.Interaction.Interaction;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.user.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Retweet extends Interaction {
    private static final Logger logger = LoggerFactory.getLogger(Retweet.class);

    private Tweet tweetOriginal;
    private String comentario;

    public Retweet(String id, Usuario emisor, Tweet tweetOriginal, String comentario) {
        super(id, emisor);
        this.tweetOriginal = tweetOriginal;
        this.comentario = comentario;
    }

    @Override
    public void ejecutar() {
        logger.info("@{} retwitteó el tweet {}",
                getEmisor().getAlias(),
                tweetOriginal.getId());
    }

    @Override
    public String getTipo() {
        return "Retweet";
    }

    public Tweet getTweetOriginal() {
        return tweetOriginal;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean esRetweetConComentario() {
        return comentario != null
                && !comentario.trim().isEmpty();

    }
}
