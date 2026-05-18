package com.twitter.exception;

public abstract class RedSocialException extends RuntimeException {

    public RedSocialException(String mensaje) {
        super(mensaje);
    }

    public RedSocialException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}