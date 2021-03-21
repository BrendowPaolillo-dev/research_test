package com.researchs.pdi.repositories;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Integer> {

    Resposta findByPerguntaAndOpcao(Pergunta pergunta, String opcao);

    List<Resposta> findByPergunta(Pergunta pergunta);

    @Query(value = "SELECT R.*" +
                   "  FROM RESPOSTA R" +
                   "  JOIN PERGUNTA P ON P.ID = R.PERGUNTA" +
                   " WHERE P.PESQUISA = ?1", nativeQuery = true)
    List<Resposta> findByPesquisa(Pesquisa pesquisa);

}
