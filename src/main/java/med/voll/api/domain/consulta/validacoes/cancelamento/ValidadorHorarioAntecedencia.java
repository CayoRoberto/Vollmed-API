package med.voll.api.domain.consulta.validacoes.cancelamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;

@Component("ValidadorHorarioAntecedenciaCancelamento")
public class ValidadorHorarioAntecedencia implements ValidadorCancelamentoDeConsulta {

  @Autowired
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
