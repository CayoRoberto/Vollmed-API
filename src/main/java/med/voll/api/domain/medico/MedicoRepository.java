package med.voll.api.domain.medico;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

  Page<Medico> findAllByAtivoTrue(Pageable paginacao);

  // Anotacao @Query para informar ao Spring que o desenvolvedor vai criar a
  // consulta no banco de dados
  // usando a sintaxe do JPQL - Java Persistence Query Language e Text block
  @Query("""
       select m from Medico m
       where
       m.ativo = true
       and
       m.especialidade = :especialidade
       and
       m.id not in(
        select c.medico.id from Consulta c
        where
        c.data = :data
       )
       order by rand()
       limit 1
      """)
  Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);

}
