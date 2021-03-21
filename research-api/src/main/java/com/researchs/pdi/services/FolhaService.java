package com.researchs.pdi.services;


import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.repositories.FolhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FolhaService {

    private final FolhaRepository folhaRepository;

    public Folha novo(Pesquisa pesquisa, int numero) {
        Folha folha = Folha.builder()
                .numero(numero)
                .pesquisa(pesquisa)
                .build();
        return folhaRepository.saveAndFlush(folha);
    }

    public List<Folha> pesquisa() {
        return folhaRepository.findAll();
    }

    public List<Folha> pesquisa(Pesquisa pesquisa) {
        return folhaRepository.findByPesquisa(pesquisa);
    }

    public void criarFolhas(Pesquisa pesquisa, int qtdeFolhas) {
        for (int i = 1; i <= qtdeFolhas; i++) {
            novo(pesquisa, i);
        }
    }

    public Integer min(Pesquisa pesquisa) {
        return folhaRepository.getMin(pesquisa);
    }

    public Integer max(Pesquisa pesquisa) {
        return folhaRepository.getMax(pesquisa);
    }

    public Folha pesquisa(Pesquisa pesquisa, int numeroFolha) {
        return folhaRepository.findByPesquisaAndNumero(pesquisa, numeroFolha);
    }

    public void apagar(Folha folha) {
        folhaRepository.delete(folha);
    }

    public void apagar(Pesquisa pesquisa) {
        for (Folha folha : pesquisa(pesquisa))
            folhaRepository.delete(folha);
    }

    public Folha pesquisa(Integer idFolha) {
        return folhaRepository.findById(idFolha).orElse(null);
    }
}
