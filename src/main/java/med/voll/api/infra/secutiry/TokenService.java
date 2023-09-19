package med.voll.api.infra.secutiry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import med.voll.api.domain.usuario.Usuario;

@Service
public class TokenService {

  // Dizer ao spring para que ele leia a variavel do aplication.properties e add
  // no atributo secret
  @Value("${api.security.token.secret}")
  private String secret;

  public String gerarToken(Usuario usuario) {
    try {
      var algoritmo = Algorithm.HMAC256(secret);
      // O nome da aplicacao
      // guarda o usuario que é dono desse token
      // data de expiracao do token
      return JWT.create()
          .withIssuer("API Voll.med")
          .withSubject(usuario.getLogin())
          .withExpiresAt(dataExpiracao())
          .sign(algoritmo);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Erro ao gerar token jwt", exception);
    }
  }

  private Instant dataExpiracao() {
    // pegar a data e hora atual somar mais duas horas e converter para instante com
    // o fuso-horario do brasil : -03:00h
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
