package com.twitter.model.tweet;

import com.twitter.exception.ContenidoInvalidoException;
import com.twitter.model.user.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Tweet implements Comparable<Tweet> {

    /** Contador estático para generar IDs únicos durante la ejecución. */
    private static long contadorId = 0;

    /** Formato de fecha para representaciones legibles. */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Límite absoluto de caracteres (UsuarioVerificado puede llegar a este máximo). */
    private static final int LIMITE_MAXIMO_CARACTERES = 4000;

    // ---------- Atributos ----------

    private final String id;
    private final Usuario autor;
    private final String contenido;
    private final LocalDateTime fechaPublicacion;
    private final List<String> hashtags;


    protected Tweet(Usuario autor, String contenido, List<String> hashtags) {
        validarAutor(autor);
        validarContenido(contenido, autor);

        this.id = generarId();
        this.autor = autor;
        this.contenido = contenido;
        this.fechaPublicacion = LocalDateTime.now();
        this.hashtags = (hashtags == null)
                ? new ArrayList<>()
                : new ArrayList<>(hashtags);  // copia defensiva
    }


    private static void validarAutor(Usuario autor) {
        if (autor == null) {
            throw new ContenidoInvalidoException(
                    "El autor del tweet no puede ser null");
        }
    }

    private static void validarContenido(String contenido, Usuario autor) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new ContenidoInvalidoException(
                    "El contenido del tweet no puede estar vacío");
        }
        if (contenido.length() > LIMITE_MAXIMO_CARACTERES) {
            throw new ContenidoInvalidoException(
                    "El contenido excede el límite absoluto de "
                            + LIMITE_MAXIMO_CARACTERES + " caracteres");
        }
        if (!autor.puedePublicarContenidoDe(contenido.length())) {
            throw new ContenidoInvalidoException(
                    "El contenido excede el límite permitido para "
                            + autor.getClass().getSimpleName()
                            + " (" + contenido.length() + " caracteres)");
        }
    }

    private static synchronized String generarId() {
        contadorId++;
        return "TWT_" + contadorId;
    }

    public abstract String getTipo();


    public String getId() {
        return id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public List<String> getHashtags() {
        return Collections.unmodifiableList(hashtags);
    }

    @Override
    public int compareTo(Tweet otro) {
        return otro.fechaPublicacion.compareTo(this.fechaPublicacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tweet)) return false;
        Tweet otro = (Tweet) obj;
        return Objects.equals(this.id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "[%s | %s | @%s | %s] %s",
                getTipo(),
                id,
                autor.getAlias(),
                fechaPublicacion.format(FORMATO_FECHA),
                contenido.length() > 60
                        ? contenido.substring(0, 57) + "..."
                        : contenido
        );
    }
}