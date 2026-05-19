package com.twitter.model.user;

public class UsuarioRegular extends Usuario {

    private static final int LIMITE_CARACTERES = 280;

    public UsuarioRegular(String id,
                          String username,
                          String email,
                          String displayName) {

        super(id, username, email, displayName);
    }


    @Override
    public int getLimiteCaracteres() {
        return LIMITE_CARACTERES;
    }

    @Override
    public String getTipoUsuario() {
        return "REGULAR";
    }
}