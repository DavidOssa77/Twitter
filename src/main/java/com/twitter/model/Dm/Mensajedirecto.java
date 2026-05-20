package com.twitter.model.Dm;

import com.twitter.model.Interaction.Interaction;
import com.twitter.model.user.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mensajedirecto extends Interaction {

    private static final Logger logger = LoggerFactory.getLogger(Mensajedirecto.class);
    private Usuario receptor;
    private String mensaje;

    public Mensajedirecto(String id, Usuario emisor, Usuario receptor, String mensaje) {
        super(id, emisor);
        this.receptor = receptor;
        this.mensaje = mensaje;
    }

    @Override
    public void ejecutar() {
        logger.info("@{} envió un mensaje directo a @{}",
                getEmisor().getAlias(),
                receptor.getAlias());
    }

    @Override
    public String getTipo() {
        return "Mensaje Directo";
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

}
