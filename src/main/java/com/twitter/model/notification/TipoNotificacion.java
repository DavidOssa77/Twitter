package com.twitter.model.notification;

import com.twitter.model.user.Usuario;

public enum TipoNotificacion {

    NUEVO_TWEET("@%s publicó un nuevo tweet"),
    NUEVO_SEGUIDOR("@%s comenzó a seguirte"),
    MENCION("@%s te mencionó en un tweet"),
    LIKE_RECIBIDO("A @%s le gustó tu tweet"),
    RETWEET_RECIBIDO("@%s retwitteó tu tweet"),
    RESPUESTA_RECIBIDA("@%s respondió a tu tweet"),
    MENSAJE_DIRECTO("@%s te envió un mensaje directo");

    private final String plantilla;

    TipoNotificacion(String plantilla) {
        this.plantilla = plantilla;
    }

    public String generarMensaje(Usuario emisor) {
        if (emisor == null) {
            throw new IllegalArgumentException(
                    "El emisor no puede ser null para generar el mensaje");
        }
        return String.format(plantilla, emisor.getAlias());
    }

    public String getPlantilla() {
        return plantilla;
    }
}