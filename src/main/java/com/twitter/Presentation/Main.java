package com.twitter.Presentation;

import com.twitter.exception.InteractionException;

import com.twitter.model.tweet.Respuesta;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.tweet.TweetSimple;

import com.twitter.model.user.Moderador;
import com.twitter.model.user.Usuario;
import com.twitter.model.user.UsuarioRegular;
import com.twitter.model.user.UsuarioVerificado;

import com.twitter.service.InteractionService;
import com.twitter.service.TweetService;
import com.twitter.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        UsuarioService usuarioService =
                new UsuarioService();

        TweetService tweetService =
                new TweetService();

        InteractionService interactionService =
                InteractionService.getInstancia();

        logger.info("===== INICIO DE PRUEBAS =====");

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

        logger.info("Usuarios creados");

        usuarioService.registrarUsuario(mateo);
        usuarioService.registrarUsuario(ana);
        usuarioService.registrarUsuario(admin);

        logger.info(
                "Usuarios registrados: {}",
                usuarioService.contarUsuarios()
        );

        usuarioService.seguirUsuario("U1", "U2");

        logger.info("Mateo sigue a Ana");

        Tweet tweet1 = new TweetSimple(ana, "Primer tweet del sistema", Arrays.asList("#java", "#twitter"));

        tweetService.publicar(tweet1);

        logger.info("Tweet publicado {}", tweet1.getId());

        Tweet respuesta = new Respuesta(mateo, "Excelente publicación", tweet1);

        tweetService.publicar(respuesta);

        logger.info("Respuesta publicada {}", respuesta.getId());

        interactionService.darLike(
                mateo,
                tweet1
        );

        interactionService.retweetear(
                mateo,
                tweet1
        );

        interactionService.enviarMensajeDirecto(
                mateo,
                ana,
                "Hola Ana"
        );

        logger.info(
                "Likes actuales: {}",
                interactionService.contarLikes(tweet1)
        );

        logger.info(
                "Retweets actuales: {}",
                interactionService.contarRetweets(tweet1)
        );

        logger.info("===== PRUEBA DOBLE LIKE =====");

        try {

            interactionService.darLike(
                    mateo,
                    tweet1
            );

        } catch (InteractionException e) {

            logger.warn(
                    "Excepción capturada: {}",
                    e.getMessage()
            );
        }

        logger.info("===== PRUEBA DM VACÍO =====");

        try {

            interactionService.enviarMensajeDirecto(
                    mateo,
                    ana,
                    "   "
            );

        } catch (InteractionException e) {

            logger.warn(
                    "Excepción capturada: {}",
                    e.getMessage()
            );
        }

        logger.info("===== PRUEBA BLOQUEO =====");

        usuarioService.bloquearUsuario(
                "U2",
                "U1"
        );

        try {

            interactionService.enviarMensajeDirecto(
                    mateo,
                    ana,
                    "Hola"
            );

        } catch (InteractionException e) {

            logger.warn(
                    "Excepción capturada: {}",
                    e.getMessage()
            );
        }

        usuarioService.desbloquearUsuario(
                "U2",
                "U1"
        );

        logger.info("===== PRUEBA SUSPENSIÓN =====");

        usuarioService.suspenderUsuario(
                "U3",
                "U2"
        );

        try {

            interactionService.enviarMensajeDirecto(
                    mateo,
                    ana,
                    "Mensaje después de suspensión"
            );

        } catch (InteractionException e) {

            logger.warn(
                    "Excepción capturada: {}",
                    e.getMessage()
            );
        }

        usuarioService.reactivarUsuario(
                "U3",
                "U2"
        );

        logger.info("===== LISTADO FINAL =====");

        for (Usuario usuario :
                usuarioService.listarUsuarios()) {

            logger.info(
                    "{}",
                    usuario
            );
        }

        for (Tweet tweet :
                tweetService.listarTodos()) {

            logger.info(
                    "{}",
                    tweet
            );
        }

        logger.info(
                "Interacciones registradas: {}",
                interactionService.getRegistro()
                        .size()
        );

        logger.info(
                "Tweets publicados: {}",
                tweetService.cantidadTweets()
        );

        logger.info(
                "===== FIN DE PRUEBAS ====="
        );
    }
}