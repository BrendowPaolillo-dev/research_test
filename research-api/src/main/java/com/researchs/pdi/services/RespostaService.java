package com.researchs.pdi.services;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import com.researchs.pdi.repositories.RespostaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RespostaService {

    private final PerguntaService perguntaService;
    private final RespostaRepository respostaRepository;

    public List<Resposta> pesquisa() {
        return respostaRepository.findAll();
    }

    public Resposta novo(Pergunta pergunta, String opcao, String descricao) {
        valida(pergunta, opcao, descricao);

        Resposta resposta = Resposta.builder()
                .pergunta(pergunta)
                .opcao(opcao)
                .descricao(descricao)
                .build();

        return respostaRepository.saveAndFlush(resposta);
    }

    private void valida(Pergunta pergunta, String opcao, String descricao) {
        Resposta perguntaAndOpcao = respostaRepository.findByPerguntaAndOpcao(pergunta, opcao);
        if (perguntaAndOpcao != null)
            throw new RuntimeException("Opção já cadastrada para esta pergunta");
    }

    public Resposta pesquisa(Pesquisa pesquisa, int numeroPergunta, String opcaoResposta) {
        Pergunta pergunta = perguntaService.pesquisa(pesquisa, numeroPergunta);
        return respostaRepository.findByPerguntaAndOpcao(pergunta, opcaoResposta);
    }

    public Resposta atualizar(Resposta resposta) {
        return respostaRepository.saveAndFlush(resposta);
    }

    public List<Resposta> pesquisa(Pergunta pergunta) {
        return respostaRepository.findByPergunta(pergunta);
    }

    public void apagar(Resposta resposta) {
        respostaRepository.delete(resposta);
    }

    public void apagar(Pesquisa pesquisa) {
        for (Resposta resposta : pesquisa(pesquisa))
            respostaRepository.delete(resposta);
    }

    private List<Resposta> pesquisa(Pesquisa pesquisa) {
        return respostaRepository.findByPesquisa(pesquisa);
    }

    public Resposta pesquisa(Integer idResposta) {
        return respostaRepository.findById(idResposta).orElse(null);
    }
}
