package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;

public record DadosDetalhmanetoPaciente(Long id, String Nome, String email, String telefone, String cpf,
    Endereco endereco) {
  public DadosDetalhmanetoPaciente(Paciente paciente) {
    this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(), paciente.getCpf(),
        paciente.getEndereco());
  }
}
