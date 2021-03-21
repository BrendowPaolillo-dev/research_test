package com.researchs.pdi.repositories;

import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolhaRepository extends JpaRepository<Folha, Integer> {

    List<Folha> findByPesquisa(Pesquisa pesquisa);

    @Query(value = "SELECT MIN(F.NUMERO) FROM FOLHA F WHERE F.PESQUISA = ?1", nativeQuery = true)
    Integer getMin(Pesquisa pesquisa);

    @Query(value = "SELECT MAX(F.NUMERO) FROM FOLHA F WHERE F.PESQUISA = ?1", nativeQuery = true)
    Integer getMax(Pesquisa pesquisa);

    Folha findByPesquisaAndNumero(Pesquisa pesquisa, Integer numero);

}
