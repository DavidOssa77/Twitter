package com.twitter.model.Interaction;

import com.twitter.model.tweet.Tweet;
import com.twitter.model.user.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Like extends Interaction {
    private static final Logger logger = LoggerFactory.getLogger(Like.class);

    private Tweet tweetObjetivo;

    public Like(String id, Usuario emisor, Tweet tweetObjetivo) {
        super(id, emisor);
        this.tweetObjetivo = tweetObjetivo;

    }

    @Override
    public void ejecutar() {
        logger.info("@{} dio Like al tweet {}",
                getEmisor().getAlias(),
                tweetObjetivo.getId());
    }

    @Override
    public String getTipo() {
        return "Like";
    }

    public Tweet getTweetObjetivo() {
        return tweetObjetivo;
    }
}
