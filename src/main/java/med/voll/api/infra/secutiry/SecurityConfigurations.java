package med.voll.api.infra.secutiry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // classe de configuracao pro spring saber
@EnableWebSecurity // avisar o spring que será personalizado as configuracoes de seguranca
public class SecurityConfigurations {

  // Esse método esta desabilitando o processo de autenticacao padrão do Spring
  // Security do csrf,
  // saindo de statefull para stateless(API Rest)
  // o token já faz essa seguranca entao nao ha necessidade
  @Bean // Anotacao que serve para expor esse metodo para o Spring
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  // Método para ensinar ao Spring qual o tipo de criptografia que esta sendo
  // utilizada no armazenamento do
  // banco de dados
  // criptografia do tipo BCrypt
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
