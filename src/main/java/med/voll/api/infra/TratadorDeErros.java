package med.voll.api.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice // Anotacão para indicar que essa classe vai ser responsavel por tratar os erros
public class TratadorDeErros {

  @ExceptionHandler(EntityNotFoundException.class) // esse método será chamado para qualquer controller do projeto
  // quando der erro de not found
  public ResponseEntity tratarErro404() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
    var erros = ex.getFieldErrors();
    return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
  }

  private record DadosErroValidacao(String campo, String mensagem) {

    public DadosErroValidacao(FieldError erro) {
      // nome do campo, msg padrao pro erro especifico
      this(erro.getField(), erro.getDefaultMessage());
    }
  }

}
