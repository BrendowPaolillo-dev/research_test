package com.researchs.pdi.services;

import com.researchs.pdi.dto.EntrevistadoDTO;
import com.researchs.pdi.dto.EntrevistadoPerguntaRespostaDTO;
import com.researchs.pdi.models.Entrevistado;
import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import com.researchs.pdi.repositories.EntrevistadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EntrevistadoService {

    private final PerguntaService perguntaService;
    private final FolhaService folhaService;
    private final RespostaService respostaService;
    private final EntrevistadoRepository entrevistadoRepository;

    public void novo(Pesquisa pesquisa, EntrevistadoDTO entrevistadoDTO) {
        valida(pesquisa, entrevistadoDTO);

        for (EntrevistadoPerguntaRespostaDTO resposta: entrevistadoDTO.getRespostas())  {
            Entrevistado entrevistado = Entrevistado.builder()
                    .pesquisa(pesquisa)
                    .folha(entrevistadoDTO.getFolha())
                    .pergunta(resposta.getPergunta())
                    .resposta(resposta.getResposta())
                    .build();

            entrevistadoRepository.saveAndFlush(entrevistado);
        }
    }

    @Transactional
    public Entrevistado novo(Entrevistado entrevistado) {
        return entrevistadoRepository.save(entrevistado);
    }

    public List<Entrevistado> pesquisa(Pesquisa pesquisa) {
        return entrevistadoRepository.findByPesquisa(pesquisa);
    }

    private void valida(Pesquisa pesquisa, EntrevistadoDTO entrevistadoDTO) {
        int qtdeRespostas = entrevistadoDTO.getRespostas().size();
        int qtdePerguntas = perguntaService.pesquisa(pesquisa).size();

        if (qtdePerguntas != qtdeRespostas)
            throw new RuntimeException("Folha " + entrevistadoDTO.getFolha().getNumero() + " não teve todas as perguntas respondidas");
    }

    public List<Entrevistado> pesquisa(Pesquisa pesquisa, Folha folha) {
        return entrevistadoRepository.findByPesquisaAndFolha(pesquisa, folha);
    }

    public void apagar(Pesquisa pesquisa) {
        for (Entrevistado entrevistado : pesquisa(pesquisa))
            entrevistadoRepository.delete(entrevistado);
    }

    @Transactional
    public Entrevistado salvar(Entrevistado entrevistado) throws Exception {
        valida(entrevistado.getPesquisa(), entrevistado.getFolha(), entrevistado.getPergunta(), entrevistado.getResposta());
        return entrevistadoRepository.save(entrevistado);
    }

    private void valida(Pesquisa pesquisa, Folha folha, Pergunta pergunta, Resposta resposta) throws Exception {
        if (pesquisa == null)
            throw new Exception("Pesquisa não encontrada");

        if (folha == null)
            throw new Exception("Folha de Pesquisa não encontrada");

        if (pergunta == null)
            throw new Exception("Pergunta não encontrada");

        if (resposta == null)
            throw new Exception("Resposta não encontrada");
    }

}
