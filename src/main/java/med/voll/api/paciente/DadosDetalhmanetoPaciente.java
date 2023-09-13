package med.voll.api.paciente;

import med.voll.api.endereco.Endereco;

public record DadosDetalhmanetoPaciente(Long id, String Nome, String email, String telefone, String cpf,
    Endereco endereco) {
  public DadosDetalhmanetoPaciente(Paciente paciente) {
    this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(), paciente.getCpf(),
        paciente.getEndereco());
  }
}
