package com.twitter.exception;

public class ContenidoInvalidoException extends RedSocialException {

    public ContenidoInvalidoException(String mensaje) {
        super(mensaje);
    }

    public ContenidoInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}