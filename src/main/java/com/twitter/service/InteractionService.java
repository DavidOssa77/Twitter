package com.twitter.service;

import com.twitter.exception.InteractionException;
import com.twitter.model.Interaction.Mensajedirecto;
import com.twitter.model.Interaction.Interaction;
import com.twitter.model.Interaction.Like;
import com.twitter.model.Interaction.Retweet;
import com.twitter.model.notification.TipoNotificacion;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.user.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class InteractionService {
    private static final Logger logger = LoggerFactory.getLogger(InteractionService.class);
    private static InteractionService instancia;
    private List<Interaction> registro;
    private NotificacionService notificacionService;

    private InteractionService() {
        this.registro = new ArrayList<>();
        this.notificacionService = new NotificacionService();
    }

    public static InteractionService getInstancia() {
        if (instancia == null) {
            instancia = new InteractionService();
        }
        return instancia;
    }

    public void darLike(Usuario emisor, Tweet tweet) {
        if (!emisor.puedeInteractuarCon(
                tweet.getAutor())) {
            throw new InteractionException(
                    "No puedes interactuar con este usuario");
        }

        if (yaDioLike(emisor, tweet)) {
            throw new InteractionException(
                    "El usuario ya dio Like a este tweet");
        }

        Like like = new Like(
                "LIKE_" + (registro.size() + 1),
                emisor,
                tweet
        );

        like.ejecutar();

        registro.add(like);

        notificacionService.suscribir(
                tweet.getAutor(),
                TipoNotificacion.LIKE_RECIBIDO
        );

        notificacionService.notificar(
                TipoNotificacion.LIKE_RECIBIDO,
                emisor,
                tweet
        );

        logger.info("Like registrado correctamente");
    }

    public void retweetear(Usuario emisor,
                           Tweet tweet) {
        if (!emisor.puedeInteractuarCon(
                tweet.getAutor())) {
            throw new InteractionException(
                    "No puedes interactuar con este usuario");
        }

        Retweet retweet = new Retweet(
                "RT_" + (registro.size() + 1),
                emisor,
                tweet,
                ""
        );

        retweet.ejecutar();

        registro.add(retweet);

        notificacionService.suscribir(
                tweet.getAutor(),
                TipoNotificacion.RETWEET_RECIBIDO
        );

        notificacionService.notificar(
                TipoNotificacion.RETWEET_RECIBIDO,
                emisor,
                tweet
        );

        logger.info("Retweet registrado correctamente");
    }

    public void enviarMensajeDirecto(Usuario emisor, Usuario receptor, String mensaje) {

        if (receptor.isSuspendido()) {
            throw new InteractionException(
                    "No se puede enviar mensajes a usuarios suspendidos");
        }

        if (!emisor.puedeInteractuarCon(receptor)) {

            throw new InteractionException(
                    "No puedes enviar mensajes a usuarios bloqueados");
        }

        if (mensaje == null
                || mensaje.trim().isEmpty()) {

            throw new InteractionException(
                    "El mensaje directo no puede estar vacío");
        }

        Mensajedirecto dm =
                new Mensajedirecto(
                        "DM_" + (registro.size() + 1),
                        emisor,
                        receptor,
                        mensaje
                );

        dm.ejecutar();

        registro.add(dm);

        notificacionService.suscribir(
                receptor,
                TipoNotificacion.MENSAJE_DIRECTO
        );

        notificacionService.notificar(
                TipoNotificacion.MENSAJE_DIRECTO,
                emisor,
                dm
        );

        logger.info("Mensaje directo enviado correctamente");
    }

    private boolean yaDioLike(Usuario emisor,
                              Tweet tweet) {

        for (Interaction interaccion : registro) {

            if (interaccion instanceof Like) {

                Like like = (Like) interaccion;

                if (like.getEmisor().equals(emisor)
                        && like.getTweetObjetivo().equals(tweet)) {

                    return true;
                }
            }
        }

        return false;
    }

    public int contarLikes(Tweet tweet) {
        int contador = 0;

        for (Interaction interaccion : registro) {

            if (interaccion instanceof Like) {

                Like like = (Like) interaccion;

                if (like.getTweetObjetivo().equals(tweet)) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public int contarRetweets(Tweet tweet) {
        int contador = 0;

        for (Interaction interaccion : registro) {

            if (interaccion instanceof Retweet) {
                Retweet retweet = (Retweet) interaccion;

                if (retweet.getTweetOriginal()
                        .equals(tweet)) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public List<Interaction> getRegistro() {
        return registro;
    }
}
