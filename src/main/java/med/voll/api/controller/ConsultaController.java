package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.CancelamentoConsulta;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;

@RestController
@RequestMapping("consultas")
public class ConsultaController {

  @Autowired
  private AgendaDeConsultas agenda;

  @Autowired
  private CancelamentoConsulta cancelarConsulta;

  @PostMapping
  public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
    var dto = agenda.agendar(dados);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity cancelarConsulta(@RequestBody @Valid DadosCancelamentoConsulta dados) {

    // Verificar se a consulta pode ser cancelada
    cancelarConsulta.cancelarConsultaMedica(dados);

    return ResponseEntity.noContent().build();
  }
}
