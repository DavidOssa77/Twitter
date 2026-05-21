package com.twitter;

import com.twitter.exception.InteractionException;

import com.twitter.model.tweet.Respuesta;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.tweet.TweetSimple;

import com.twitter.model.user.Usuario;
import com.twitter.model.user.UsuarioRegular;
import com.twitter.model.user.UsuarioVerificado;
import com.twitter.model.user.Moderador;

import com.twitter.service.PlataformaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        PlataformaService plataforma =
                PlataformaService.getInstancia();

        logger.info("===== INICIO =====");

        Usuario mateo =
                new UsuarioRegular(
                        "U1",
                        "mateo",
                        "mateo@email.com",
                        "Mateo"
                );

        Usuario ana =
                new UsuarioVerificado(
                        "U2",
                        "ana",
                        "ana@email.com",
                        "Ana"
                );

        Usuario admin =
                new Moderador(
                        "U3",
                        "admin",
                        "admin@email.com",
                        "Administrador",
                        2
                );

        plataforma.registrarUsuario(mateo);

        plataforma.registrarUsuario(ana);

        plataforma.registrarUsuario(admin);

        logger.info(
                "Usuarios registrados {}",
                plataforma.contarUsuarios()
        );

        plataforma.seguirUsuario(
                "U1",
                "U2"
        );

        Tweet tweet1 =
                new TweetSimple(
                        ana,
                        "Primer tweet",
                        Arrays.asList(
                                "#java",
                                "#poo"
                        )
                );

        plataforma.publicarTweet(
                tweet1
        );

        Tweet respuesta =
                new Respuesta(
                        mateo,
                        "Excelente publicación",
                        tweet1
                );

        plataforma.publicarTweet(
                respuesta
        );

        plataforma.darLike(
                mateo,
                tweet1
        );

        plataforma.retweetear(
                mateo,
                tweet1
        );

        plataforma.enviarMensajeDirecto(
                mateo,
                ana,
                "Hola Ana"
        );

        logger.info(
                "Likes {}",
                plataforma.contarLikes(
                        tweet1
                )
        );

        logger.info(
                "Retweets {}",
                plataforma.contarRetweets(
                        tweet1
                )
        );

        try {

            plataforma.darLike(
                    mateo,
                    tweet1
            );

        }

        catch (
                InteractionException e
        ) {

            logger.warn(
                    "{}",
                    e.getMessage()
            );

        }

        plataforma.bloquearUsuario(
                "U2",
                "U1"
        );

        try {

            plataforma.enviarMensajeDirecto(
                    mateo,
                    ana,
                    "Hola"
            );

        }

        catch (
                InteractionException e
        ) {

            logger.warn(
                    "{}",
                    e.getMessage()
            );

        }

        plataforma.desbloquearUsuario(
                "U2",
                "U1"
        );

        plataforma.suspenderUsuario(
                "U3",
                "U2"
        );

        try {

            plataforma.enviarMensajeDirecto(
                    mateo,
                    ana,
                    "Mensaje"
            );

        }

        catch (
                InteractionException e
        ) {

            logger.warn(
                    "{}",
                    e.getMessage()
            );

        }

        logger.info(
                "Tweets publicados {}",
                plataforma.contarTweets()
        );

        logger.info(
                "===== FIN ====="
        );

    }

}