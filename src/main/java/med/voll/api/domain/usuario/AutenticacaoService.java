package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Essa classe é um componente do tipo servico nesse caso servico de
         // autenticacao
// Interface UserDetailsService - representa o servico de autenticacao
public class AutenticacaoService implements UserDetailsService {

  @Autowired
  private UsuarioRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    return repository.findByLogin(username);

  }

}
