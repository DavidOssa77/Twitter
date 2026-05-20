package com.twitter.model.Interaction;

import com.twitter.model.user.Usuario;

import java.time.LocalDateTime;

public abstract class Interaction {
    private String id;
    private Usuario emisor;
    private LocalDateTime fecha;

    public Interaction(String id, Usuario emisor) {
        this.id = id;
        this.emisor = emisor;
        this.fecha = LocalDateTime.now();
    }

    public abstract void ejecutar();

    public abstract String getTipo();

    public String getId() {
        return id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

}
