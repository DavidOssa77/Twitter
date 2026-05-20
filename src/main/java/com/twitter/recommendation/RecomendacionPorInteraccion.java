package com.twitter.recommendation;

import com.twitter.model.Interaction.Interaction;
import com.twitter.model.Interaction.Like;
import com.twitter.model.Interaction.Mensajedirecto;
import com.twitter.model.Interaction.Retweet;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.user.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RecomendacionPorInteraccion
        implements EstrategiaRecomendacion {

    @Override
    public List<Usuario> calcularUsuarios(
            Usuario usuario,
            List<Usuario> usuarios,
            List<Interaction> interacciones
    ) {

        List<Usuario> recomendaciones = new ArrayList<>();

        for (Interaction interaccion : interacciones) {

            if (interaccion.getEmisor().equals(usuario)) {

                Usuario posibleRecomendado =
                        obtenerUsuarioRelacionado(interaccion);

                if (posibleRecomendado != null
                        && !posibleRecomendado.equals(usuario)
                        && !usuario.getSeguidos().contains(posibleRecomendado)
                        && !recomendaciones.contains(posibleRecomendado)
                        && usuario.puedeInteractuarCon(posibleRecomendado)) {

                    recomendaciones.add(posibleRecomendado);
                }
            }
        }

        return recomendaciones;
    }

    private Usuario obtenerUsuarioRelacionado(
            Interaction interaccion
    ) {

        if (interaccion instanceof Like) {

            Like like = (Like) interaccion;

            Tweet tweet = like.getTweetObjetivo();

            return tweet.getAutor();
        }

        if (interaccion instanceof Retweet) {

            Retweet retweet = (Retweet) interaccion;

            Tweet tweet = retweet.getTweetOriginal();

            return tweet.getAutor();
        }

        if (interaccion instanceof Mensajedirecto) {

            Mensajedirecto mensaje =
                    (Mensajedirecto) interaccion;

            return mensaje.getReceptor();
        }

        return null;
    }

    @Override
    public String getNombre() {

        return "Recomendación por interacción";
    }
}