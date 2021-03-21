package com.researchs.pdi;

import com.researchs.pdi.config.FunctionalTest;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.PerguntaService;
import com.researchs.pdi.services.PesquisaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@FunctionalTest
class PerguntasTest {

    private final PesquisaService pesquisaService;
    private final PerguntaService perguntaService;

    @Autowired
    PerguntasTest(PesquisaService pesquisaService,
                  PerguntaService perguntaService) {
        this.pesquisaService = pesquisaService;
        this.perguntaService = perguntaService;
    }

    @Test
    void deveriaExistirUmaPerguntaCadastrada() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisa, 1, "Idade");

        List<Pergunta> all = perguntaService.pesquisa();
        assertEquals(1, all.size(), "Deveria existir uma pergunta");
    }

    @Test
    void deveriamExistirDuasPerguntasCadastradas() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisa, 1, "Idade");
        perguntaService.novo(pesquisa, 2, "Sexo");

        List<Pergunta> all = perguntaService.pesquisa();
        assertEquals(2, all.size());
    }

    @Test
    void naoPermitirCadastrarDuasPerguntasComMesmoNumero() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());

        String msg = null;

        perguntaService.novo(pesquisa, 1, "Idade");
        try {
            perguntaService.novo(pesquisa, 1, "Sexo");
        }
        catch(RuntimeException e) {
            msg = e.getMessage();
        }
        assertNotNull(msg);

        List<Pergunta> all = perguntaService.pesquisa();
        assertEquals(1, all.size());
    }

    @Test
    void permitirCadastrarDuasPerguntasComMesmoNumeroCasoSejaDePesquisaDiferente() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        Pesquisa pesquisaAuxiliar = pesquisaService.novo("Pesquisa Auxiliar", new Date());

        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaAuxiliar, 1, "Sexo");

        List<Pergunta> all = perguntaService.pesquisa();
        assertEquals(2, all.size());
    }

    @Test
    void naoPermitirCadastrarDuasPerguntasComMesmaDescricao() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());

        String msg = null;

        perguntaService.novo(pesquisa, 1, "Idade");
        try {
            perguntaService.novo(pesquisa, 2, "Idade");
        }
        catch(RuntimeException e) {
            msg = e.getMessage();
        }
        assertEquals("Pergunta já cadastrada", msg);

        List<Pergunta> all = perguntaService.pesquisa();
        assertEquals(1, all.size());
    }

    @Test
    void permitirCadastrarDuasPerguntasComMesmaDescricaoParaPesquisasDiferentes() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        Pesquisa pesquisaAuxiliar = pesquisaService.novo("Pesquisa Auxiliar", new Date());

        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaAuxiliar, 1, "Idade");

        List<Pergunta> all = perguntaService.pesquisa();
        assertEquals(2, all.size());
    }

    @Test
    void consultaQtdePerguntasPorPesquisa() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        Pesquisa pesquisaAuxiliar = pesquisaService.novo("Pesquisa Auxiliar", new Date());

        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaTeste, 2, "Sexo");
        perguntaService.novo(pesquisaAuxiliar, 1, "Idade");

        List<Pergunta> allTeste = perguntaService.pesquisa(pesquisaTeste);
        assertEquals(2, allTeste.size());

        for(Pergunta pergunta: allTeste) {
            assertEquals(pesquisaTeste, pergunta.getPesquisa(), "Pesquisa Retornada");
        }
    }

    @Test
    void atualizarPergunta() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaTeste, 2, "Sexo");

        Pergunta pergunta = perguntaService.pesquisa(pesquisaTeste, 1);
        pergunta.setDescricao("Município");
        perguntaService.atualizar(pergunta);

        Pergunta perguntaAtualizada = perguntaService.pesquisa(pesquisaTeste, 1);

        assertEquals(pesquisaTeste, perguntaAtualizada.getPesquisa(), "Pesquisa com pergunta Atualizada");
        assertEquals(Integer.valueOf(1), perguntaAtualizada.getNumero(), "Número da pergunta Atualizada");
        assertEquals("Município", perguntaAtualizada.getDescricao(), "Nova Descrição pergunta Atualizada");
    }

    @Test
    void naoDeveAtualizarNumeroDePerguntaParaNumeroExistente() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaTeste, 2, "Sexo");

        String msg = null;

        Pergunta pergunta = perguntaService.pesquisa(pesquisaTeste, 1);
        pergunta.setNumero(2);
        try {
            perguntaService.atualizar(pergunta);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        assertNotNull(msg);
    }

}
