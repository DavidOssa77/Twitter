package com.twitter.service;

import com.twitter.exception.UsuarioException;
import com.twitter.model.user.Moderador;
import com.twitter.model.user.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UsuarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioService.class);

    private Map<String, Usuario> usuarios;

    public UsuarioService() {
        this.usuarios = new HashMap<>();
    }

    public void registrarUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        if (usuarios.containsKey(usuario.getId())) {
            throw new UsuarioException("Ya existe un usuario con ese ID");
        }

        usuarios.put(usuario.getId(), usuario);

        logger.info("Usuario registrado: {}", usuario.getUsername());
    }

    public Usuario buscarPorId(String id) {

        Usuario usuario = usuarios.get(id);

        if (usuario == null) {
            throw new UsuarioException("No existe un usuario con el ID: " + id);
        }

        return usuario;
    }

    public Usuario buscarPorUsername(String username) {

        for (Usuario usuario : usuarios.values()) {

            if (usuario.getUsername().equalsIgnoreCase(username)) {
                return usuario;
            }
        }

        throw new UsuarioException(
                "No existe un usuario con username: " + username
        );
    }

    public List<Usuario> listarUsuarios() {

        return new ArrayList<>(usuarios.values());
    }

    public List<Usuario> listarOrdenadosPorUsername() {

        List<Usuario> lista = new ArrayList<>(usuarios.values());

        Collections.sort(lista);

        return lista;
    }

    public List<Usuario> listarOrdenadosPorSeguidores() {

        List<Usuario> lista = new ArrayList<>(usuarios.values());

        lista.sort(new Comparator<Usuario>() {

            @Override
            public int compare(Usuario u1, Usuario u2) {

                int seguidoresU1 = u1.getSeguidores().size();
                int seguidoresU2 = u2.getSeguidores().size();

                return seguidoresU2 - seguidoresU1;
            }
        });

        return lista;
    }

    public List<Usuario> topInfluyentes(int cantidad) {

        if (cantidad <= 0) {
            throw new UsuarioException("La cantidad debe ser mayor que cero");
        }

        List<Usuario> lista = listarOrdenadosPorSeguidores();

        if (cantidad > lista.size()) {
            cantidad = lista.size();
        }

        return lista.subList(0, cantidad);
    }

    public void seguirUsuario(String idSeguidor, String idSeguido) {

        Usuario seguidor = buscarPorId(idSeguidor);
        Usuario seguido = buscarPorId(idSeguido);

        seguidor.seguir(seguido);

        logger.info("{} empezó a seguir a {}",
                seguidor.getUsername(),
                seguido.getUsername());
    }

    public void dejarDeSeguirUsuario(String idSeguidor, String idSeguido) {

        Usuario seguidor = buscarPorId(idSeguidor);
        Usuario seguido = buscarPorId(idSeguido);

        seguidor.dejarDeSeguir(seguido);

        logger.info("{} dejó de seguir a {}",
                seguidor.getUsername(),
                seguido.getUsername());
    }

    public void bloquearUsuario(String idUsuario, String idBloqueado) {

        Usuario usuario = buscarPorId(idUsuario);
        Usuario bloqueado = buscarPorId(idBloqueado);

        usuario.bloquear(bloqueado);

        logger.warn("{} bloqueó a {}",
                usuario.getUsername(),
                bloqueado.getUsername());
    }

    public void desbloquearUsuario(String idUsuario, String idBloqueado) {

        Usuario usuario = buscarPorId(idUsuario);
        Usuario bloqueado = buscarPorId(idBloqueado);

        usuario.desbloquear(bloqueado);

        logger.info("{} desbloqueó a {}",
                usuario.getUsername(),
                bloqueado.getUsername());
    }

    public void suspenderUsuario(String idModerador, String idUsuario) {

        Usuario posibleModerador = buscarPorId(idModerador);
        Usuario usuario = buscarPorId(idUsuario);

        if (!(posibleModerador instanceof Moderador)) {
            throw new UsuarioException("Solo un moderador puede suspender usuarios");
        }

        Moderador moderador = (Moderador) posibleModerador;

        moderador.suspenderUsuario(usuario);

        logger.warn("Usuario suspendido desde UsuarioService: {}",
                usuario.getUsername());
    }

    public void reactivarUsuario(String idModerador, String idUsuario) {

        Usuario posibleModerador = buscarPorId(idModerador);
        Usuario usuario = buscarPorId(idUsuario);

        if (!(posibleModerador instanceof Moderador)) {
            throw new UsuarioException(
                    "Solo un moderador puede reactivar usuarios"
            );
        }

        Moderador moderador = (Moderador) posibleModerador;

        moderador.reactivarUsuario(usuario);

        logger.warn("Usuario reactivado desde UsuarioService: {}",
                usuario.getUsername());
    }

    public int contarUsuarios() {
        return usuarios.size();
    }
}
