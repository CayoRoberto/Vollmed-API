package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(

                @NotBlank // verifica se não ta vazio e não nulo e só serve para campos Strings
                String nome,

                @NotBlank @Email // valida se é um email
                String email,

                @NotBlank String telefone,

                @NotBlank @Pattern(regexp = "\\d{4,6}") // verifica que são digitos e que podem ser 4 ou 6 digitos
                String crm,

                @NotNull // não pode ser null
                Especialidade especialidade,

                @NotNull @Valid // verificar outro DTO
                DadosEndereco endereco) {

}
