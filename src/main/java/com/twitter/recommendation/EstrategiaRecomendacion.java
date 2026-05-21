package com.twitter.recommendation;

import com.twitter.model.Interaction.Interaction;
import com.twitter.model.user.Usuario;

import java.util.List;

public interface EstrategiaRecomendacion {

    List<Usuario> calcularUsuarios(
            Usuario usuario,
            List<Usuario> usuarios,
            List<Interaction> interacciones
    );

    String getNombre();
}