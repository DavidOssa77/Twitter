package com.twitter.service;

import com.twitter.model.notification.Notificacion;
import com.twitter.model.notification.Observador;
import com.twitter.model.notification.TipoNotificacion;
import com.twitter.model.user.Usuario;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class NotificacionService {


    private final Map<TipoNotificacion, Set<Observador>> observadoresPorTipo;


    public NotificacionService() {
        this.observadoresPorTipo = new EnumMap<>(TipoNotificacion.class);

        for (TipoNotificacion tipo : TipoNotificacion.values()) {
            observadoresPorTipo.put(tipo, new HashSet<>());
        }
    }

    public void suscribir(Observador observador, TipoNotificacion tipo) {
        validarNoNull(observador, tipo);
        observadoresPorTipo.get(tipo).add(observador);
    }

    public void suscribirATodo(Observador observador) {
        if (observador == null) {
            throw new IllegalArgumentException(
                    "El observador no puede ser null");
        }
        for (TipoNotificacion tipo : TipoNotificacion.values()) {
            observadoresPorTipo.get(tipo).add(observador);
        }
    }

    public void desuscribir(Observador observador, TipoNotificacion tipo) {
        validarNoNull(observador, tipo);
        observadoresPorTipo.get(tipo).remove(observador);
    }

    public void desuscribirDeTodo(Observador observador) {
        if (observador == null) {
            throw new IllegalArgumentException(
                    "El observador no puede ser null");
        }
        for (Set<Observador> conjunto : observadoresPorTipo.values()) {
            conjunto.remove(observador);
        }
    }

    public Notificacion notificar(TipoNotificacion tipo, Usuario emisor,
                                  Object referencia) {
        if (tipo == null || emisor == null) {
            throw new IllegalArgumentException(
                    "El tipo y el emisor no pueden ser null");
        }
        Notificacion notificacion = new Notificacion(tipo, emisor, referencia);

        Set<Observador> suscritos = observadoresPorTipo.get(tipo);
        for (Observador observador : suscritos) {
            observador.actualizar(notificacion.getMensaje());
        }
        return notificacion;
    }

    public int contarSuscritos(TipoNotificacion tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }
        return observadoresPorTipo.get(tipo).size();
    }

    public boolean estaSuscrito(Observador observador, TipoNotificacion tipo) {
        if (observador == null || tipo == null) {
            return false;
        }
        return observadoresPorTipo.get(tipo).contains(observador);
    }

    public Set<Observador> getSuscritos(TipoNotificacion tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }
        return Collections.unmodifiableSet(observadoresPorTipo.get(tipo));
    }

    private static void validarNoNull(Observador observador,
                                      TipoNotificacion tipo) {
        if (observador == null) {
            throw new IllegalArgumentException(
                    "El observador no puede ser null");
        }
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de notificación no puede ser null");
        }
    }
}