package com.researchs.pdi.services;

import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.repositories.PesquisaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PesquisaService {

    private final PesquisaRepository pesquisaRepository;
    private final EntrevistadoService entrevistadoService;
    private final RespostaService respostaService;
    private final PerguntaService perguntaService;
    private final FolhaService folhaService;

    @Transactional
    public Pesquisa novo(String descricao, Date data) {
        valida(descricao, data);

        Pesquisa pesquisa = Pesquisa.builder()
                .descricao(descricao)
                .data(data)
                .build();

        return pesquisaRepository.save(pesquisa);
    }

    private void valida(String descricao, Date data) {
        if (pesquisaRepository.findByDescricaoAndData(descricao, data) != null)
            throw new RuntimeException("Pesquisa já cadastrada");
    }

    @Transactional
    public Pesquisa atualizar(Pesquisa pesquisa) {
        return pesquisaRepository.saveAndFlush(pesquisa);
    }

    public Pesquisa pesquisa(String nomePesquisa, Date data) {
        return pesquisaRepository.findByDescricaoAndData(nomePesquisa, data);
    }

    public List<Pesquisa> pesquisa() {
        return pesquisaRepository.findAll();
    }

    public List<Pesquisa> pesquisa(String descricaoPesquisa) {
        return pesquisaRepository.findByDescricao(descricaoPesquisa);
    }

    public List<Pesquisa> pesquisa(Date data) {
        return pesquisaRepository.findByData(data);
    }

    @Transactional
    public Pesquisa atualizar(Pesquisa pesquisa, String descricao, Date data) {
        valida(pesquisa, descricao, data);
        pesquisa.setDescricao(descricao);
        pesquisa.setData(data);
        return pesquisaRepository.saveAndFlush(pesquisa);
    }

    private void valida(Pesquisa pesquisa, String descricao, Date data) {
        Pesquisa pesquisaGet = pesquisaRepository.findByDescricaoAndData(descricao, data);
        if (pesquisaGet != null && !pesquisa.equals(pesquisaGet))
            throw new RuntimeException("Pesquisa já cadastrada");
    }

    @Transactional
    public void apagarTodasAsPesquisas() {
        for (Pesquisa pesquisa : pesquisa()) {
            entrevistadoService.apagar(pesquisa);
            respostaService.apagar(pesquisa);
            perguntaService.apagar(pesquisa);
            folhaService.apagar(pesquisa);
            pesquisaRepository.delete(pesquisa);
        }

    }

    public Pesquisa pesquisa(Integer idPesquisa) {
        return pesquisaRepository.findById(idPesquisa).orElse(null);
    }
}
