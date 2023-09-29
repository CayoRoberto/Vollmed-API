package med.voll.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest // Simular teste controller
@AutoConfigureMockMvc // Avisar ao Spring para injetar a requisicao por mock mvc
@AutoConfigureJsonTesters // Para o Spring poder injetar o JacksonTester
public class ConsultaControllerTest {

  @Autowired
  private MockMvc mvc;

  // Classe que vai converter o json para testar a requisicao
  // json que chega na API
  @Autowired
  private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

  // json que é retornado pela API
  @Autowired
  private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

  @MockBean // Anotacao que indica para a classe não injetar pelo banco de dados e sim fazer
            // um mock
  private AgendaDeConsultas agendaDeConsultas;

  @Test
  @DisplayName("Deveria devolver código http 400 quando informações estão invalidadas")
  @WithMockUser // Anotação para que o Spring Security deixe o test ser feito como se tivesse
                // logado
  void testAgendarcenario1() throws Exception {
    var response = mvc.perform(post("/consultas")).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria devolver código http 200 quando informações estão válidas")
  @WithMockUser // Anotação para que o Spring Security deixe o test ser feito como se tivesse
                // logado
  void testAgendarcenario2() throws Exception {
    var data = LocalDateTime.now().plusHours(1);
    var especialidade = Especialidade.CARDIOLOGIA;
    var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 5l, data);
    // Utilizando o mockito
    when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

    var response = mvc
        .perform(
            post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAgendamentoConsultaJson.write(
                    new DadosAgendamentoConsulta(2l, 5l, data, especialidade)).getJson()))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    var jsonEsperado = dadosDetalhamentoConsultaJson.write(
        dadosDetalhamento).getJson();

    assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
  }
}
