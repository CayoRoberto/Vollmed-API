package med.voll.api.domain.medico;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;

@DataJpaTest // esta anotação é utilizada para testar uma interface Repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Dizendo para o Spring para fazer o teste utilizando o banco de dados SGBD não
// o o banco de dados em memória
@ActiveProfiles("test") // Dizer para o Spring para ler o propreties test
public class MedicoRepositoryTest {

  @Autowired
  private MedicoRepository medicoRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("Deveria devolver null quando unico medico cadastrado nao esta disponivel na data")
  void testEscolherMedicoAleatorioLivreNaDataCenario1() {
    // given ou arrange
    var proximaSegundaAs10h = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
    var medico = cadastrarMedico("Medico", "medico@vollmed", "15789", Especialidade.CARDIOLOGIA);
    var paciente = cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
    cadastrarConsulta(medico, paciente, proximaSegundaAs10h);

    // when ou act
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA,
        proximaSegundaAs10h);

    // then ou assert
    assertThat(medicoLivre).isNull();
  }

  @Test
  @DisplayName("Deveria devolver medico quando ele estiver disponivel na data")
  void testEscolherMedicoAleatorioLivreNaDataCenario2() {
    // given ou arrange
    var proximaSegundaAs10h = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
    var medico = cadastrarMedico("Medico", "medico@vollmed", "15789", Especialidade.CARDIOLOGIA);
    // when ou act
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA,
        proximaSegundaAs10h);
    // then ou assert
    assertThat(medicoLivre).isEqualTo(medico);
  }

  private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
    em.persist(new Consulta(null, medico, paciente, data, null));

  }

  private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
    var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
    em.persist(medico);
    return medico;
  }

  private Paciente cadastrarPaciente(String nome, String email, String cpf) {
    var paciente = new Paciente(dadosPaciente(nome, email, cpf));
    em.persist(paciente);
    return paciente;
  }

  private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
    return new DadosCadastroMedico(
        nome,
        email,
        "98785623",
        crm,
        especialidade,
        dadosEndereco());
  }

  private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
    return new DadosCadastroPaciente(
        nome,
        email,
        "98756423",
        cpf,
        dadosEndereco());
  }

  private DadosEndereco dadosEndereco() {
    return new DadosEndereco("rua xpto", "bairro", "000000", "São Luís", "MA", null, null);
  }

}
