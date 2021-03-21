package com.researchs.pdi;

import com.researchs.pdi.config.FunctionalTest;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import com.researchs.pdi.services.PerguntaService;
import com.researchs.pdi.services.PesquisaService;
import com.researchs.pdi.services.RespostaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FunctionalTest
class RespostasTest {

    private final PesquisaService pesquisaService;
    private final PerguntaService perguntaService;
    private final RespostaService respostaService;

    @Autowired
    RespostasTest(PesquisaService pesquisaService,
                  PerguntaService perguntaService,
                  RespostaService respostaService) {

        this.pesquisaService = pesquisaService;
        this.perguntaService = perguntaService;
        this.respostaService = respostaService;
    }

    @Test
    void deveriaExistirUmaRespostaCadastrada() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");

        List<Resposta> all = respostaService.pesquisa();
        assertEquals(1, all.size(), "Deveria existir uma resposta");
    }

    @Test
    void deveriamExistirDuasRespostasCadastradas() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        List<Resposta> all = respostaService.pesquisa();
        assertEquals(2, all.size(), "Deveriam existir duas respostas");
    }

    @Test
    void naoPermitirCadastrarDuasRespostasDeMesmaOpcaoParaAMesmaPergunta() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");

        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");

        assertThrows(
                RuntimeException.class,
                () -> respostaService.novo(pergunta, "a", "Entre 24 e 29 anos"),
                "Deveria ter uma mensagem de erro"
        );

        List<Resposta> all = respostaService.pesquisa();
        assertEquals(1, all.size(), "Deveria existir uma resposta");
    }

    @Test
    void permitirCadastrarDuasRespostasDeMesmaOpcaoParaPerguntasDiferentes() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta1 = perguntaService.novo(pesquisa, 1, "Nota Opcao A");
        respostaService.novo(pergunta1, "a", "1");
        respostaService.novo(pergunta1, "b", "2");
        respostaService.novo(pergunta1, "c", "3");
        respostaService.novo(pergunta1, "d", "4");
        respostaService.novo(pergunta1, "e", "5");

        Pergunta pergunta2 = perguntaService.novo(pesquisa, 2, "Nota Opcao B");
        respostaService.novo(pergunta2, "a", "1");
        respostaService.novo(pergunta2, "b", "2");
        respostaService.novo(pergunta2, "c", "3");
        respostaService.novo(pergunta2, "d", "4");
        respostaService.novo(pergunta2, "e", "5");

        List<Resposta> all = respostaService.pesquisa();
        assertEquals(10, all.size(), "Deveria existir 10 respostas");
    }

    @Test
    void permitirAtualizarResposta() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        Resposta resposta = respostaService.pesquisa(pesquisa, 1, "b");
        resposta.setDescricao("Entre 24 e 39 anos");
        respostaService.atualizar(resposta);

        Resposta respostaAssert = respostaService.pesquisa(pesquisa, 1, "b");
        assertEquals("Entre 24 e 39 anos", respostaAssert.getDescricao(), "Descrição resposta");
    }

    @Test
    void naoPermitirAtualizarRespostaParaOpcaoExistente() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        Resposta resposta = respostaService.pesquisa(pesquisa, 1, "b");
        resposta.setOpcao("a");

        String msg = null;
        try {
            respostaService.atualizar(resposta);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }
        assertNotNull(msg);
    }

    @Test
    void consultarRespostaPelaOpcao() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        Resposta resposta = respostaService.pesquisa(pesquisa, 1, "b");
        assertEquals("b", resposta.getOpcao());
        assertEquals("Faixa etária", resposta.getPergunta().getDescricao());
    }

}
