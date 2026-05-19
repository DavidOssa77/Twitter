package com.twitter.model.user;

public class UsuarioVerificado extends Usuario {

    private static final int LIMITE_CARACTERES = 4000;

    private boolean badgeAzul;

    public UsuarioVerificado(String id,
                             String username,
                             String email,
                             String displayName) {

        super(id, username, email, displayName);

        this.badgeAzul = true;
    }

    @Override
    public int getLimiteCaracteres() {
        return LIMITE_CARACTERES;
    }

    @Override
    public String getTipoUsuario() {
        return "VERIFICADO";
    }

    public boolean tieneBadgeAzul() {
        return badgeAzul;
    }
}