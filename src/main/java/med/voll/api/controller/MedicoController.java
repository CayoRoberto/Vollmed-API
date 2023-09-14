package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

  @Autowired // Injeção de dependencias
  private MedicoRepository repository;

  @PostMapping
  @Transactional // transação ativa com o bd
  public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uBuilder) {
    var medico = new Medico(dados);
    repository.save(medico);
    // vai armazenar a url com a classe do SpringBoot UriComponentsBuilder
    // pegar o parametro dinamico (id) do medico que esta sendo gerado
    var uri = uBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

    return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico)); // cod http: 201 -> registro criado
  }

  @GetMapping // devolver os dados
  public ResponseEntity<Page<DadosListagemMedico>> listar(
      @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
    // por padrão caso, não tenha escrito na url definer esses valores default de
    // paginação
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    return ResponseEntity.ok(page); // cod http: 200
  }

  @PutMapping
  @Transactional
  public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
    var medico = repository.getReferenceById(dados.id());
    medico.atualizarInformacoes(dados);

    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
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
  public ResponseEntity excluir(@PathVariable Long id) {
    var medico = repository.getReferenceById(id);
    medico.excluir();

    return ResponseEntity.noContent().build(); // retorna para a requisicao que o id excluido não está mais contido
                                               // cod http: 204 -> para excluir
  }

  // Detalhar um medico
  @GetMapping("/{id}") // id paramentro dinamico parametro da anotacao @DeleteMapping
  public ResponseEntity detalhar(@PathVariable Long id) {
    var medico = repository.getReferenceById(id);

    // retorna o detalhamento de um medico do id selecionado
    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));

  }
}
