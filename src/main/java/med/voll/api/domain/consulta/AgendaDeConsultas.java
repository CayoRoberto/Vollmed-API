package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

// A classe Service executa as regras de negócio e validações da aplicação
@Service // Identificar para o Spring que essa classe é um servico. Carregue essa classe!
public class AgendaDeConsultas {

  @Autowired // Para o Spring fazer a injeção no atributo
  private ConsultaRepository consultaRepository;

  @Autowired
  private MedicoRepository medicoRepository;

  @Autowired
  private PacienteRepository pacienteRepository;

  public void agendar(DadosAgendamentoConsulta dados) {
    // verificar se existe o medico e paciente que estão sendo colados para o
    // agendamento da consulta
    if (!pacienteRepository.existsById(dados.idPaciente())) {
      throw new ValidacaoException("Id do paciente informado não existe!");
    }
    // como na regra de negocio fala que para marcar a consulta o médico é opcional,
    // então ele pode já vir nulo
    if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
      throw new ValidacaoException("Id do médico informado não existe!");
    }
    // pegar a entidade paciente por meio do repository
    var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

    // como o médico é opcional, deve pegar aleatoriamente no banco de dados
    var medico = escolherMedico(dados);

    var consulta = new Consulta(null, medico, paciente, dados.data());
    consultaRepository.save(consulta);
  }

  // Verifica se ta chegando um id medico pela requisicao, caso seja nulo, pegar
  // um id medico aleatorio do banco de dados
  private Medico escolherMedico(DadosAgendamentoConsulta dados) {
    // Primeiro é feito a checagem se esta vindo um id medico
    if (dados.idMedico() != null) {
      return medicoRepository.getReferenceById(dados.idMedico());
    }
    if (dados.especialidade() == null) {
      throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
    }
    return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
  }
}
