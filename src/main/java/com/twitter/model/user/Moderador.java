package com.twitter.model.user;

import com.twitter.exception.UsuarioException;
import com.twitter.model.tweet.Tweet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Moderador extends Usuario {

    private static final Logger logger =
            LoggerFactory.getLogger(Moderador.class);

    private static final int LIMITE_CARACTERES = 280;

    private int nivelPermiso;

    public Moderador(String id,
                     String username,
                     String email,
                     String displayName,
                     int nivelPermiso) {

        super(id, username, email, displayName);

        this.nivelPermiso = nivelPermiso;
    }


    @Override
    public int getLimiteCaracteres() {
        return LIMITE_CARACTERES;
    }

    @Override
    public String getTipoUsuario() {
        return "MODERADOR";
    }

    public void eliminarTweet(Tweet tweet) {

        if (tweet == null) {
            throw new UsuarioException(
                    "El tweet no puede ser null"
            );
        }

        logger.warn(
                "El moderador {} eliminó el tweet {}",
                getUsername(),
                tweet.getId()
        );
    }

    public void suspenderUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        if (this.equals(usuario)) {
            throw new UsuarioException("Un moderador no puede suspenderse a sí mismo");
        }

        if (nivelPermiso < 2) {
            throw new UsuarioException(
                    "El moderador no tiene permisos suficientes"
            );
        }

        usuario.suspender();


    }

    public void reactivarUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        usuario.reactivar();

    }

    public void reportarContenido(String contenidoId) {

        logger.info("El moderador {} reportó el contenido {}",
                getUsername(),
                contenidoId);
    }

    public int getNivelPermiso() {
        return nivelPermiso;
    }

    public void setNivelPermiso(int nivelPermiso) {
        this.nivelPermiso = nivelPermiso;
    }
}