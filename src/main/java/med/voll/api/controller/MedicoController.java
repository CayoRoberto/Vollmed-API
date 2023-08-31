package med.voll.api.controller;

//import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

  @Autowired // Injeção de dependencias
  private MedicoRepository repository;

  @PostMapping
  @Transactional // transação ativa com o bd
  public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
    repository.save(new Medico(dados));
  }

  @GetMapping // devolver os dados
  public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
    // por padrão caso, não tenha escrito na url definer esses valores default de
    // paginação
    return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
  }

  @PutMapping
  @Transactional
  public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
    var medico = repository.getReferenceById(dados.id());
    medico.atualizarInformacoes(dados);
  }

  // @DeleteMapping("/{id}") // id paramentro dinamico parametro da anotacao
  // @DeleteMapping
  // @Transactional
  // public void excluir(@PathVariable Long id) {
  // // A anotação @PathVarieble indica que o id que o metodo excluir recebe é o
  // // mesmo id da anotação @DeleteMapping
  // repository.deleteById(id);

  // }

  // exclusão lógica
  @DeleteMapping("/{id}") // id paramentro dinamico parametro da anotacao @DeleteMapping
  @Transactional
  public void excluir(@PathVariable Long id) {
    var medico = repository.getReferenceById(id);
    medico.excluir();
  }

}
