package com.twitter.service;

import com.twitter.model.Interaction.Interaction;
import com.twitter.model.user.Usuario;
import com.twitter.recommendation.EstrategiaRecomendacion;

import java.util.List;

public class RecomendacionService {

    private EstrategiaRecomendacion estrategia;

    public RecomendacionService(
            EstrategiaRecomendacion estrategia
    ) {

        this.estrategia = estrategia;
    }

    public void setEstrategia(
            EstrategiaRecomendacion estrategia
    ) {

        this.estrategia = estrategia;
    }

    public List<Usuario> recomendarUsuarios(
            Usuario usuario,
            List<Usuario> usuarios,
            List<Interaction> interacciones
    ) {

        return estrategia.calcularUsuarios(
                usuario,
                usuarios,
                interacciones
        );
    }

    public String getNombreEstrategia() {

        return estrategia.getNombre();
    }
}