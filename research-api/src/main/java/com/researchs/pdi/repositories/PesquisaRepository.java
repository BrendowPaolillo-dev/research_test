package com.researchs.pdi.repositories;

import com.researchs.pdi.models.Pesquisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PesquisaRepository extends JpaRepository<Pesquisa, Integer> {

    List<Pesquisa> findByDescricao(String descricao);

    Pesquisa findByDescricaoAndData(String descricao, Date data);

    List<Pesquisa> findByData(Date data);

}
