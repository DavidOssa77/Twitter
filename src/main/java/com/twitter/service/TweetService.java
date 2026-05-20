package com.twitter.service;

import com.twitter.exception.ContenidoInvalidoException;
import com.twitter.exception.ContenidoNoEncontradoException;
import com.twitter.model.tweet.Respuesta;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.tweet.TweetSimple;
import com.twitter.model.user.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TweetService {

    private static final Logger logger =
            LoggerFactory.getLogger(TweetService.class);

    private final Map<String, Tweet> tweets;

    public TweetService() {
        this.tweets = new LinkedHashMap<>();
        logger.info("TweetService inicializado");
    }

    public void publicar(Tweet tweet){

        if (tweet == null){
            logger.warn("intento de publicar un tweet nulo");
            throw new ContenidoInvalidoException("No se puede publicar un tweet nulo");
        }

        if (tweets.containsKey(tweet.getId())){
            logger.warn("Intento de publicar un tweet duplicado", tweet.getId());
            throw new ContenidoInvalidoException("Ya existe un tweet con el ID: " + tweet.getId());
        }
        tweets.put(tweet.getId(), tweet);
        logger.info("Tweet {} publicado por @{}", tweet.getId(), tweet.getAutor().getAlias());
    }

    public void eliminar(String id){
        if (!tweets.containsKey(id)){
            logger.warn("Intento de eliminar un tweet inexistente", id);
            throw new ContenidoNoEncontradoException("No existe un tweet con el id " + id);
        }

        tweets.remove(id);
        logger.info("Tweet eliminado ", id);
    }

    public Tweet buscarPorId(String id){
        Tweet tweet = tweets.get(id);
        if (tweet == null){
            logger.warn("Búsqueda de un tweet inexistente ", id);
            throw new ContenidoNoEncontradoException("No existe un tweet con el id: " + id);
        }
        return tweet;
    }

    public boolean existe(String id){
        return tweets.containsKey(id);
    }

    public List<Tweet> buscarPorAutor(Usuario autor){
        List<Tweet> resultado = new ArrayList<>();
        for (Tweet tweet : tweets.values()){
            if (tweet.getAutor().equals(autor)){
                resultado.add(tweet);
            }
        }
        logger.debug("buscarPorAutor encontró {} tweets", resultado.size());
        return resultado;
    }

    public List<Tweet> buscarPorHashtag(String hashtag) {
        List<Tweet> resultado = new ArrayList<>();
        for (Tweet tweet : tweets.values()) {
            if (tweet.getHashtags().contains(hashtag)) {
                resultado.add(tweet);
            }
        }
        logger.debug("buscarPorHashtag('{}') encontró {} tweets", hashtag, resultado.size());
        return resultado;
    }

    public List<Tweet> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Las fechas del rango no pueden ser null");
        }
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        List<Tweet> resultado = new ArrayList<>();
        for (Tweet tweet : tweets.values()) {
            LocalDateTime fecha = tweet.getFechaPublicacion();
            if (!fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
                resultado.add(tweet);
            }
        }
        logger.debug("filtrarPorRangoDeFechas encontró " + resultado.size() + " tweets");
        return resultado;
    }

    public List<Tweet> filtrarTweetsSimples(){
        List<Tweet> resultado = new ArrayList<>();
        for (Tweet tweet : tweets.values()){
            if (tweet instanceof TweetSimple){
                resultado.add(tweet);
            }
        }
            return resultado;
    }

    public List<Tweet> filtrarRespuestas(){
        List<Tweet> resultado = new ArrayList<>();
        for (Tweet tweet : tweets.values()){
            if (tweet instanceof Respuesta){
                resultado.add(tweet);
            }
        }
        return resultado;
    }

    public List<Tweet> listarTodos(){
        return Collections.unmodifiableList(new ArrayList<>(tweets.values()));
    }

    public List<Tweet> listarPorFechaDescendente() {
        List<Tweet> resultado = new ArrayList<>(tweets.values());
        Collections.sort(resultado);  // usa el compareTo de Tweet
        return resultado;
    }

    public int cantidadTweets() {
        return tweets.size();
    }
}
