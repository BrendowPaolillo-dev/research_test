package com.researchs.pdi.repositories;

import com.researchs.pdi.models.Entrevistado;
import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntrevistadoRepository extends JpaRepository<Entrevistado, Integer>{

    List<Entrevistado> findByPesquisa(Pesquisa pesquisa);

    List<Entrevistado> findByPesquisaAndFolha(Pesquisa pesquisa, Folha folha);
}
