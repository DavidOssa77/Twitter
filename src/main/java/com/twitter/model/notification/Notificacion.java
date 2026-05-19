package com.twitter.model.notification;

import com.twitter.model.user.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Notificacion {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TipoNotificacion tipo;
    private final String mensaje;
    private final Usuario emisor;
    private final LocalDateTime fecha;
    private final Object referencia;
    private boolean leida;


    public Notificacion(TipoNotificacion tipo, Usuario emisor, Object referencia){
        if (tipo == null){
            throw new IllegalArgumentException("El tipo de notifiacion no puede ser null");
        }

        if (emisor == null){
            throw new IllegalArgumentException("El emisor de la notificacion no puede ser null");
        }

        this.tipo = tipo;
        this.emisor = emisor;
        this.referencia = referencia;
        this.mensaje = tipo.generarMensaje(emisor);
        this.fecha = LocalDateTime.now();
        this.leida = false;

    }

    public void marcarComoLeida(){
        this.leida = true;
    }

    public TipoNotificacion getTipo(){
        return tipo;
    }

    public String getMensaje(){
        return mensaje;
    }

    public Usuario getEmisor(){
        return emisor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Object getReferencia() {
        return referencia;
    }

    public boolean isLeida() {
        return leida;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Notificacion)) {
            return false;
        }
        Notificacion otra = (Notificacion) obj;
        return tipo == otra.tipo
                && Objects.equals(emisor, otra.emisor)
                && Objects.equals(fecha, otra.fecha);
    }

    public int hashCode(){
        return Objects.hash(tipo, emisor, fecha);
    }

    public String toString() {
        return String.format("[%s | %s] %s%s",
                tipo,
                fecha.format(FORMATO_FECHA),
                mensaje,
                leida ? " (leída)" : "");
    }
}
