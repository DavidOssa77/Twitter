package com.twitter.service;

import com.twitter.model.notification.Notificacion;
import com.twitter.model.notification.Observador;
import com.twitter.model.notification.TipoNotificacion;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.user.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class PlataformaService {

    private static final Logger logger =
            LoggerFactory.getLogger(PlataformaService.class);

    private static PlataformaService instancia;

    private UsuarioService usuarioService;
    private TweetService tweetService;
    private InteractionService interaccionService;
    private NotificacionService notificacionService;

    private PlataformaService() {
        this.usuarioService = new UsuarioService();
        this.tweetService = new TweetService();
        this.interaccionService = InteractionService.getInstancia();
        this.notificacionService = new NotificacionService();

        logger.info("PlataformaService inicializado");
    }

    public static PlataformaService getInstancia() {

        if (instancia == null) {
            instancia = new PlataformaService();
        }

        return instancia;
    }

    // ---------------- USUARIOS ----------------

    public void registrarUsuario(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorId(String id) {
        return usuarioService.buscarPorId(id);
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        return usuarioService.buscarPorUsername(username);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public List<Usuario> listarUsuariosOrdenadosPorUsername() {
        return usuarioService.listarOrdenadosPorUsername();
    }

    public List<Usuario> listarUsuariosOrdenadosPorSeguidores() {
        return usuarioService.listarOrdenadosPorSeguidores();
    }

    public List<Usuario> topInfluyentes(int cantidad) {
        return usuarioService.topInfluyentes(cantidad);
    }

    public void seguirUsuario(String idSeguidor, String idSeguido) {
        usuarioService.seguirUsuario(idSeguidor, idSeguido);
    }

    public void dejarDeSeguirUsuario(String idSeguidor, String idSeguido) {
        usuarioService.dejarDeSeguirUsuario(idSeguidor, idSeguido);
    }

    public void bloquearUsuario(String idUsuario, String idBloqueado) {
        usuarioService.bloquearUsuario(idUsuario, idBloqueado);
    }

    public void desbloquearUsuario(String idUsuario, String idBloqueado) {
        usuarioService.desbloquearUsuario(idUsuario, idBloqueado);
    }

    public void suspenderUsuario(String idModerador, String idUsuario) {
        usuarioService.suspenderUsuario(idModerador, idUsuario);
    }

    public void reactivarUsuario(String idModerador, String idUsuario) {
        usuarioService.reactivarUsuario(idModerador, idUsuario);
    }

    public int contarUsuarios() {
        return usuarioService.contarUsuarios();
    }

    // ---------------- TWEETS ----------------

    public void publicarTweet(Tweet tweet) {
        tweetService.publicar(tweet);
    }

    public void eliminarTweet(String id) {
        tweetService.eliminar(id);
    }

    public Tweet buscarTweetPorId(String id) {
        return tweetService.buscarPorId(id);
    }

    public boolean existeTweet(String id) {
        return tweetService.existe(id);
    }

    public List<Tweet> buscarTweetsPorAutor(Usuario autor) {
        return tweetService.buscarPorAutor(autor);
    }

    public List<Tweet> buscarTweetsPorHashtag(String hashtag) {
        return tweetService.buscarPorHashtag(hashtag);
    }

    public List<Tweet> filtrarTweetsPorRangoDeFechas(LocalDateTime desde,
                                                     LocalDateTime hasta) {
        return tweetService.filtrarPorRangoDeFechas(desde, hasta);
    }

    public List<Tweet> filtrarTweetsSimples() {
        return tweetService.filtrarTweetsSimples();
    }

    public List<Tweet> filtrarRespuestas() {
        return tweetService.filtrarRespuestas();
    }

    public List<Tweet> listarTodosLosTweets() {
        return tweetService.listarTodos();
    }

    public List<Tweet> listarTweetsPorFechaDescendente() {
        return tweetService.listarPorFechaDescendente();
    }

    public int contarTweets() {
        return tweetService.cantidadTweets();
    }

    // ---------------- INTERACCIONES ----------------

    public void darLike(Usuario emisor, Tweet tweet) {
        interaccionService.darLike(emisor, tweet);
    }

    public void retweetear(Usuario emisor, Tweet tweet) {
        interaccionService.retweetear(emisor, tweet);
    }

    public void enviarMensajeDirecto(Usuario emisor,
                                     Usuario receptor,
                                     String mensaje) {
        interaccionService.enviarMensajeDirecto(emisor, receptor, mensaje);
    }

    public int contarLikes(Tweet tweet) {
        return interaccionService.contarLikes(tweet);
    }

    public int contarRetweets(Tweet tweet) {
        return interaccionService.contarRetweets(tweet);
    }

    // ---------------- NOTIFICACIONES ----------------

    public void suscribirNotificacion(Observador observador,
                                      TipoNotificacion tipo) {
        notificacionService.suscribir(observador, tipo);
    }

    public void suscribirATodasLasNotificaciones(Observador observador) {
        notificacionService.suscribirATodo(observador);
    }

    public void desuscribirNotificacion(Observador observador,
                                        TipoNotificacion tipo) {
        notificacionService.desuscribir(observador, tipo);
    }

    public void desuscribirDeTodasLasNotificaciones(Observador observador) {
        notificacionService.desuscribirDeTodo(observador);
    }

    public Notificacion notificar(TipoNotificacion tipo,
                                  Usuario emisor,
                                  Object referencia) {
        return notificacionService.notificar(tipo, emisor, referencia);
    }

    public int contarSuscritos(TipoNotificacion tipo) {
        return notificacionService.contarSuscritos(tipo);
    }

    public boolean estaSuscrito(Observador observador,
                                TipoNotificacion tipo) {
        return notificacionService.estaSuscrito(observador, tipo);
    }

    public Set<Observador> getSuscritos(TipoNotificacion tipo) {
        return notificacionService.getSuscritos(tipo);
    }

    // ---------------- GETTERS DE SERVICIOS ----------------

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public TweetService getTweetService() {
        return tweetService;
    }

    public InteractionService getInteractionService() {
        return interaccionService;
    }

    public NotificacionService getNotificacionService() {
        return notificacionService;
    }
}