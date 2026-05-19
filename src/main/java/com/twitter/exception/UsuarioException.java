package com.twitter.exception;

public class UsuarioException extends RedSocialException {

    public UsuarioException(String mensaje) {
        super(mensaje);
    }

    public UsuarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
