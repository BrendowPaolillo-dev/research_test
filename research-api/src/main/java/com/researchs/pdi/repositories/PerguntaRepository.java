package com.researchs.pdi.repositories;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerguntaRepository extends JpaRepository<Pergunta, Integer> {

    Pergunta findByPesquisaAndNumero(Pesquisa pesquisa, Integer numero);

    List<Pergunta> findByPesquisaAndDescricao(Pesquisa pesquisa, String descricao);

    List<Pergunta> findByPesquisa(Pesquisa pesquisa);

}
