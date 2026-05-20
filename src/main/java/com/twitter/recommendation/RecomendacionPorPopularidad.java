package com.twitter.recommendation;

import com.twitter.model.Interaction.Interaction;
import com.twitter.model.user.Usuario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecomendacionPorPopularidad implements EstrategiaRecomendacion {

    @Override
    public List<Usuario> calcularUsuarios(
            Usuario usuario,
            List<Usuario> usuarios,
            List<Interaction> interacciones
    ) {

        List<Usuario> recomendaciones = new ArrayList<>();

        for (Usuario candidato : usuarios) {

            if (!candidato.equals(usuario)
                    && !usuario.getSeguidos().contains(candidato)
                    && usuario.puedeInteractuarCon(candidato)) {

                recomendaciones.add(candidato);
            }
        }

        recomendaciones.sort(new Comparator<Usuario>() {

            @Override
            public int compare(Usuario u1, Usuario u2) {

                return u2.getSeguidores().size()
                        - u1.getSeguidores().size();
            }
        });

        return recomendaciones;
    }

    @Override
    public String getNombre() {

        return "Recomendación por popularidad";
    }
}