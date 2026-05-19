package com.twitter.model.user;

import com.twitter.exception.UsuarioException;
import com.twitter.model.notification.Observador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Usuario implements Comparable<Usuario>, Observador {

    private static final Logger logger =
            LoggerFactory.getLogger(Usuario.class);

    private String id;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private LocalDate fechaRegistro;

    private boolean suspendido;

    protected Set<Usuario> seguidores;
    protected Set<Usuario> seguidos;
    protected Set<Usuario> bloqueados;

    public Usuario(String id, String username, String email, String displayName) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.displayName = displayName;

        this.bio = "";
        this.fechaRegistro = LocalDate.now();

        this.suspendido = false;

        this.seguidores = new HashSet<>();
        this.seguidos = new HashSet<>();
        this.bloqueados = new HashSet<>();
    }

    public boolean puedePublicar() {
        return !suspendido;
    }

    public abstract int getLimiteCaracteres();

    public abstract String getTipoUsuario();

    public void seguir(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        if (this.equals(usuario)) {
            throw new UsuarioException(
                    "Un usuario no puede seguirse a sí mismo"
            );
        }

        if (this.suspendido) {
            throw new UsuarioException("El usuario está suspendido");
        }

        if (usuario.suspendido) {
            throw new UsuarioException(
                    "No se puede seguir a un usuario suspendido"
            );
        }

        if (this.bloqueados.contains(usuario)
                || usuario.bloqueados.contains(this)) {

            throw new UsuarioException(
                    "No se puede seguir a un usuario bloqueado"
            );
        }

        this.seguidos.add(usuario);
        usuario.seguidores.add(this);
    }

    public void dejarDeSeguir(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        this.seguidos.remove(usuario);
        usuario.seguidores.remove(this);
    }

    public void bloquear(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        if (this.equals(usuario)) {
            throw new UsuarioException(
                    "Un usuario no puede bloquearse a sí mismo"
            );
        }

        this.bloqueados.add(usuario);

        this.seguidos.remove(usuario);
        this.seguidores.remove(usuario);

        usuario.seguidos.remove(this);
        usuario.seguidores.remove(this);
    }

    public void desbloquear(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioException("El usuario no puede ser null");
        }

        this.bloqueados.remove(usuario);
    }

    public boolean esBloqueadoPor(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        return usuario.bloqueados.contains(this);
    }

    public boolean esSeguidoPor(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        return this.seguidores.contains(usuario);
    }

    public boolean puedeInteractuarCon(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        return !this.suspendido
                && !usuario.suspendido
                && !this.bloqueados.contains(usuario)
                && !usuario.bloqueados.contains(this);
    }

    public void suspender() {

        this.suspendido = true;

        logger.warn("El usuario {} fue suspendido",
                this.username);
    }

    public void reactivar() {

        this.suspendido = false;

        logger.info("El usuario {} fue reactivado",
                this.username);
    }

    @Override
    public void actualizar(String mensaje) {

        logger.info("Notificación para @{}: {}",
                this.username,
                mensaje);
    }

    @Override
    public int compareTo(Usuario otro) {

        return this.username.compareToIgnoreCase(
                otro.username
        );
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Usuario)) {
            return false;
        }

        Usuario usuario = (Usuario) obj;

        return Objects.equals(this.id, usuario.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {

        return "Usuario{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", tipo='" + getTipoUsuario() + '\'' +
                ", suspendido=" + suspendido +
                ", seguidores=" + seguidores.size() +
                ", seguidos=" + seguidos.size() +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAlias() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isSuspendido() {
        return suspendido;
    }

    public Set<Usuario> getSeguidores() {
        return seguidores;
    }

    public Set<Usuario> getSeguidos() {
        return seguidos;
    }

    public Set<Usuario> getBloqueados() {
        return bloqueados;
    }

    public boolean puedePublicarContenidoDe(int cantidadCaracteres) {

        if (!puedePublicar()) {
            return false;
        }

        return cantidadCaracteres <= getLimiteCaracteres();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}