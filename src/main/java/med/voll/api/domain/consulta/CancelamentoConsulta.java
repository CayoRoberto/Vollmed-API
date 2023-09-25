package med.voll.api.domain.consulta;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorCancelamentoDeConsulta;

@Service
public class CancelamentoConsulta {

  @Autowired
  private ConsultaRepository consultaRepository;

  @Autowired
  private ValidadorCancelamentoDeConsulta validadorCancelamentoDeConsulta;

  public void cancelarConsultaMedica(DadosCancelamentoConsulta dados) {
    if (dados.idConsulta() == null) {
      throw new ValidacaoException("Id de consulta informado não existe");
    }

    validadorCancelamentoDeConsulta.validar(dados);

    consultaRepository.deleteById(dados.idConsulta());
  }

}
