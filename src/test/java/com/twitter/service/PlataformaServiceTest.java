package com.twitter.service;

import com.twitter.exception.InteractionException;
import com.twitter.model.tweet.Tweet;
import com.twitter.model.tweet.TweetSimple;
import com.twitter.model.user.Moderador;
import com.twitter.model.user.Usuario;
import com.twitter.model.user.UsuarioRegular;
import com.twitter.model.user.UsuarioVerificado;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlataformaServiceTest {

    @Test
    void debeRetornarLaMismaInstanciaSingleton() {

        PlataformaService p1 = PlataformaService.getInstancia();
        PlataformaService p2 = PlataformaService.getInstancia();

        assertSame(p1, p2);
    }

    @Test
    void debeRegistrarUsuariosCorrectamente() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario usuario = new UsuarioRegular(
                "TEST_U1",
                "test_user_1",
                "test1@email.com",
                "Test User 1"
        );

        plataforma.registrarUsuario(usuario);

        Usuario encontrado =
                plataforma.buscarUsuarioPorId("TEST_U1");

        assertEquals(usuario, encontrado);
    }

    @Test
    void debePublicarTweetCorrectamente() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario autor = new UsuarioVerificado(
                "TEST_U2",
                "autor_test",
                "autor@email.com",
                "Autor Test"
        );

        plataforma.registrarUsuario(autor);

        Tweet tweet = new TweetSimple(
                autor,
                "Este es un tweet de prueba"
        );

        plataforma.publicarTweet(tweet);

        Tweet encontrado =
                plataforma.buscarTweetPorId(tweet.getId());

        assertEquals(tweet, encontrado);
    }

    @Test
    void debeDarLikeCorrectamente() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario autor = new UsuarioVerificado(
                "TEST_U3",
                "autor_like",
                "autorlike@email.com",
                "Autor Like"
        );

        Usuario emisor = new UsuarioRegular(
                "TEST_U4",
                "emisor_like",
                "emisorlike@email.com",
                "Emisor Like"
        );

        plataforma.registrarUsuario(autor);
        plataforma.registrarUsuario(emisor);

        Tweet tweet = new TweetSimple(
                autor,
                "Tweet para recibir like"
        );

        plataforma.publicarTweet(tweet);

        plataforma.darLike(emisor, tweet);

        assertEquals(1, plataforma.contarLikes(tweet));
    }

    @Test
    void noDebePermitirDobleLike() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario autor = new UsuarioVerificado(
                "TEST_U5",
                "autor_doble_like",
                "autordoble@email.com",
                "Autor Doble Like"
        );

        Usuario emisor = new UsuarioRegular(
                "TEST_U6",
                "emisor_doble_like",
                "emisordoble@email.com",
                "Emisor Doble Like"
        );

        plataforma.registrarUsuario(autor);
        plataforma.registrarUsuario(emisor);

        Tweet tweet = new TweetSimple(
                autor,
                "Tweet para probar doble like"
        );

        plataforma.publicarTweet(tweet);

        plataforma.darLike(emisor, tweet);

        assertThrows(
                InteractionException.class,
                () -> plataforma.darLike(emisor, tweet)
        );
    }

    @Test
    void debeRetweetearCorrectamente() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario autor = new UsuarioVerificado(
                "TEST_U7",
                "autor_retweet",
                "autorretweet@email.com",
                "Autor Retweet"
        );

        Usuario emisor = new UsuarioRegular(
                "TEST_U8",
                "emisor_retweet",
                "emisorretweet@email.com",
                "Emisor Retweet"
        );

        plataforma.registrarUsuario(autor);
        plataforma.registrarUsuario(emisor);

        Tweet tweet = new TweetSimple(
                autor,
                "Tweet para retweet"
        );

        plataforma.publicarTweet(tweet);

        plataforma.retweetear(emisor, tweet);

        assertEquals(1, plataforma.contarRetweets(tweet));
    }

    @Test
    void debeEnviarMensajeDirectoCorrectamente() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario emisor = new UsuarioRegular(
                "TEST_U9",
                "emisor_dm",
                "emisordm@email.com",
                "Emisor DM"
        );

        Usuario receptor = new UsuarioRegular(
                "TEST_U10",
                "receptor_dm",
                "receptordm@email.com",
                "Receptor DM"
        );

        plataforma.registrarUsuario(emisor);
        plataforma.registrarUsuario(receptor);

        assertDoesNotThrow(
                () -> plataforma.enviarMensajeDirecto(
                        emisor,
                        receptor,
                        "Hola, este es un DM de prueba"
                )
        );
    }

    @Test
    void noDebePermitirMensajeDirectoVacio() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario emisor = new UsuarioRegular(
                "TEST_U11",
                "emisor_dm_vacio",
                "emisordmvacio@email.com",
                "Emisor DM Vacío"
        );

        Usuario receptor = new UsuarioRegular(
                "TEST_U12",
                "receptor_dm_vacio",
                "receptordmvacio@email.com",
                "Receptor DM Vacío"
        );

        plataforma.registrarUsuario(emisor);
        plataforma.registrarUsuario(receptor);

        assertThrows(
                InteractionException.class,
                () -> plataforma.enviarMensajeDirecto(
                        emisor,
                        receptor,
                        "   "
                )
        );
    }

    @Test
    void noDebePermitirInteractuarConUsuarioBloqueado() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario autor = new UsuarioVerificado(
                "TEST_U13",
                "autor_bloqueo",
                "autorbloqueo@email.com",
                "Autor Bloqueo"
        );

        Usuario emisor = new UsuarioRegular(
                "TEST_U14",
                "emisor_bloqueo",
                "emisorbloqueo@email.com",
                "Emisor Bloqueo"
        );

        plataforma.registrarUsuario(autor);
        plataforma.registrarUsuario(emisor);

        Tweet tweet = new TweetSimple(
                autor,
                "Tweet de usuario que bloquea"
        );

        plataforma.publicarTweet(tweet);

        plataforma.bloquearUsuario("TEST_U13", "TEST_U14");

        assertThrows(
                InteractionException.class,
                () -> plataforma.darLike(emisor, tweet)
        );
    }

    @Test
    void noDebePermitirEnviarDmAUsuarioSuspendido() {

        PlataformaService plataforma = PlataformaService.getInstancia();

        Usuario moderador = new Moderador(
                "TEST_U15",
                "admin_test",
                "admin@email.com",
                "Admin Test",
                2
        );

        Usuario emisor = new UsuarioRegular(
                "TEST_U16",
                "emisor_suspendido",
                "emisorsuspendido@email.com",
                "Emisor Suspendido"
        );

        Usuario receptor = new UsuarioRegular(
                "TEST_U17",
                "receptor_suspendido",
                "receptorsuspendido@email.com",
                "Receptor Suspendido"
        );

        plataforma.registrarUsuario(moderador);
        plataforma.registrarUsuario(emisor);
        plataforma.registrarUsuario(receptor);

        plataforma.suspenderUsuario("TEST_U15", "TEST_U17");

        assertThrows(
                InteractionException.class,
                () -> plataforma.enviarMensajeDirecto(
                        emisor,
                        receptor,
                        "Hola"
                )
        );
    }
}