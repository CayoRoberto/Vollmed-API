package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.Especialidade;

public record DadosAgendamentoConsulta(

                Long idMedico,
                @NotNull Long idPaciente,

                @NotNull @Future // Anotacao que diz que a data tem que ser no futuro
                LocalDateTime data,

                Especialidade especialidade) {

}
