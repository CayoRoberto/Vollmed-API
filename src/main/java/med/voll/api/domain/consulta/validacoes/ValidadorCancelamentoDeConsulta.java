package med.voll.api.domain.consulta.validacoes;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;

@Component
public class ValidadorCancelamentoDeConsulta {

  private ConsultaRepository repository;

  public void validar(DadosCancelamentoConsulta dados) {

    var consulta = repository.getReferenceById(dados.idConsulta());
    var dataConsulta = consulta.getData();
    var agora = LocalDateTime.now();
    var diferencaEmHora = Duration.between(agora, dataConsulta).toHours();

    if (diferencaEmHora < 24) {
      throw new ValidacaoException(
          "Tempo de cancelamento excedido!Cancelamento não poder feito com menos de 24h para a consulta.");
    }

  }
}
