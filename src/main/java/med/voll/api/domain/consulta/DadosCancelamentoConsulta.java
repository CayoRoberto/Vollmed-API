package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
                Long idConsulta,

                @NotNull String motivo) {

}
