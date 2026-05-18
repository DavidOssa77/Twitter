package com.twitter.model.tweet;


import com.twitter.exception.ContenidoInvalidoException;
import com.twitter.model.user.Usuario;

import java.util.List;

public class Respuesta extends Tweet{

    private final Tweet tweetPadre;
    private final int nivelHilo;


    public Respuesta(Usuario autor, String contenido, Tweet tweetPadre, List<String> hashtags){
        super(autor, contenido, hashtags);
        if (tweetPadre==null){
            throw new ContenidoInvalidoException("Una respuesta debe estar asociada a un tweet");
        }
        this.tweetPadre = tweetPadre;
        this.nivelHilo = calcularNivelHilo(tweetPadre);
    }

    public Respuesta(Usuario autor, String contenido, Tweet tweetPadre){
        this(autor, contenido, tweetPadre, null);
    }

    private static int calcularNivelHilo(Tweet padre) {
        if (padre instanceof Respuesta) {
            return ((Respuesta) padre).getNivelHilo() + 1;
        }
        return 1;
    }

    public String getTipo() {
        return "Respuesta";
    }

    private int getNivelHilo(){
        return nivelHilo;
    }

    public boolean esHiloDe(Tweet tweetObjetivo){
        if (tweetObjetivo==null){
            return false;
        }

        Tweet actual = this.tweetPadre;
        while (actual != null){
            if (actual.equals(tweetObjetivo)){
                return true;
            }
            actual = (actual instanceof Respuesta) ? ((Respuesta) actual).getTweetPadre() : null;
        }
        return false;
    }

    private Tweet getTweetPadre(){
        return tweetPadre;
    }

    public String toString() {
        return super.toString()
                + " (responde a " + tweetPadre.getId()
                + ", nivel " + nivelHilo + ")";
    }
}
