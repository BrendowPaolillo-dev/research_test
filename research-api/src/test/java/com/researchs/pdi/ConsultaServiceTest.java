package com.researchs.pdi;

import com.researchs.pdi.config.FunctionalTest;
import com.researchs.pdi.dto.PesquisaDTO;
import com.researchs.pdi.entrevistado.TemplatePesquisa;
import com.researchs.pdi.http.SendPesquisaGateway;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.FolhaService;
import com.researchs.pdi.services.PerguntaService;
import com.researchs.pdi.services.PesquisaService;
import com.researchs.pdi.services.RespostaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static com.researchs.pdi.entrevistado.TemplatePesquisa.resposta;
import static com.researchs.pdi.utils.DateUtils.getDate;
import static com.researchs.pdi.utils.DateUtils.getParse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@FunctionalTest
class ConsultaServiceTest {

    public static final Date DATA_PADRAO = getDate(getParse("20/11/2016"));

    private final PesquisaService pesquisaService;
    private final PerguntaService perguntaService;
    private final RespostaService respostaService;
    private final FolhaService folhaService;
    private final SendPesquisaGateway service;

    @Autowired
    ConsultaServiceTest(PesquisaService pesquisaService,
                        PerguntaService perguntaService,
                        RespostaService respostaService,
                        FolhaService folhaService,
                        SendPesquisaGateway service) {
        this.pesquisaService = pesquisaService;
        this.perguntaService = perguntaService;
        this.respostaService = respostaService;
        this.folhaService = folhaService;
        this.service = service;
    }

    @Test
    void deveriaRetornarPesquisaMontada() {
        getEnvironmentDeTresFolhasDePesquisa();

        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        new TemplatePesquisa()
                .pesquisa("Pesquisa Teste", DATA_PADRAO)
                .pergunta(1, "Faixa Etária",
                        resposta("a", "Entre 16 e 24 anos"),
                        resposta("b", "Entre 25 e 34 anos"),
                        resposta("c", "Entre 35 e 49 anos"),
                        resposta("d", "Entre 50 e 60 anos"),
                        resposta("e", "Acima de 60 anos")
                        )
                .pergunta(2, "Sexo",
                        resposta("a", "M"),
                        resposta("b", "F")
                )
                .pergunta(3, "Salário familiar",
                        resposta("a", "Até 1 salário mínimo"),
                        resposta("b", "Entre 1 e 3 salários mínimos"),
                        resposta("c", "Entre 4 e 7 salários mínimos"),
                        resposta("d", "Entre 8 e 10 salários mínimos"),
                        resposta("e", "Acima de 10 salários mínimos")
                )
                .folhas(3)
                .check(estruturaBasica);
    }

    @Test
    void deveriaRetornarPesquisaDiferente() {
        getEnvironmentDeTresFolhasDePesquisa();

        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);
        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("Pesquisa Homologação Errada", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M"),
                            resposta("b", "F")
                    )
                    .pergunta(3, "Salário familiar",
                            resposta("a", "Até 1 salário mínimo"),
                            resposta("b", "Entre 1 e 3 salários mínimos"),
                            resposta("c", "Entre 4 e 7 salários mínimos"),
                            resposta("d", "Entre 8 e 10 salários mínimos"),
                            resposta("e", "Acima de 10 salários mínimos")
                    )
                    .folhas(3)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Pesquisa Esperada:Pesquisa Homologação Errada\nPesquisa Retornada: PESQUISA TESTE";
        assertEquals(msgEsperada, msg, "Pesquisas Diferentes");
    }

    @Test
    void deveriaRetornarFolhaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();
        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("PESQUISA TESTE", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M"),
                            resposta("b", "F")
                    )
                    .pergunta(3, "Salário familiar",
                            resposta("a", "Até 1 salário mínimo"),
                            resposta("b", "Entre 1 e 3 salários mínimos"),
                            resposta("c", "Entre 4 e 7 salários mínimos"),
                            resposta("d", "Entre 8 e 10 salários mínimos"),
                            resposta("e", "Acima de 10 salários mínimos")
                    )
                    .folhas(4)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Quantidade de Folhas diferentes. Esperado: 4 - Atual: 3";
        assertEquals(msgEsperada, msg, "Folha não esperada");
    }

    @Test
    void deveriaRetornarFolhaNaoEncontradaInverso() {
        getEnvironmentDeTresFolhasDePesquisa();
        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("Pesquisa Teste", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M"),
                            resposta("b", "F")
                    )
                    .pergunta(3, "Salário familiar",
                            resposta("a", "Até 1 salário mínimo"),
                            resposta("b", "Entre 1 e 3 salários mínimos"),
                            resposta("c", "Entre 4 e 7 salários mínimos"),
                            resposta("d", "Entre 8 e 10 salários mínimos"),
                            resposta("e", "Acima de 10 salários mínimos")
                    )
                    .folhas(2)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Quantidade de Folhas diferentes. Esperado: 2 - Atual: 3";
        assertEquals(msgEsperada, msg, "Folha não esperada");
    }

    @Test
    void deveriaRetornarPerguntaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();
        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("Pesquisa Teste", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M"),
                            resposta("b", "F")
                    )
                    .pergunta(3, "Salário familiar",
                            resposta("a", "Até 1 salário mínimo"),
                            resposta("b", "Entre 1 e 3 salários mínimos"),
                            resposta("c", "Entre 4 e 7 salários mínimos"),
                            resposta("d", "Entre 8 e 10 salários mínimos"),
                            resposta("e", "Acima de 10 salários mínimos")
                    )
                    .pergunta(4, "Pergunta não existente",
                            resposta("a", "Indefinido")
                    )
                    .folhas(3)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Pergunta Pergunta não existente deveria existir.";
        assertEquals(msgEsperada, msg, "Pergunta não esperada");
    }

    @Test
    void deveriaRetornarPerguntaNaoEncontradaInverso() {
        getEnvironmentDeTresFolhasDePesquisa();
        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("Pesquisa Teste", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M"),
                            resposta("b", "F")
                    )
                    .folhas(3)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Pergunta Salário familiar deveria existir.";
        assertEquals(msgEsperada, msg, "Pergunta não esperada");
    }


    @Test
    void deveriaRetornarRespostaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();
        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("Pesquisa Teste", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M"),
                            resposta("b", "F"),
                            resposta("c", "Indefinido")
                    )
                    .pergunta(3, "Salário familiar",
                            resposta("a", "Até 1 salário mínimo"),
                            resposta("b", "Entre 1 e 3 salários mínimos"),
                            resposta("c", "Entre 4 e 7 salários mínimos"),
                            resposta("d", "Entre 8 e 10 salários mínimos"),
                            resposta("e", "Acima de 10 salários mínimos")
                    )
                    .folhas(3)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Resposta c da pergunta 2 não encontrada.";
        assertEquals(msgEsperada, msg, "Resposta não esperada");
    }

    @Test
    void deveriaRetornarRespostaNaoEncontradaInverso() {
        getEnvironmentDeTresFolhasDePesquisa();
        PesquisaDTO estruturaBasica = service.getEstruturaBasica(null);

        String msg = null;
        try {
            new TemplatePesquisa()
                    .pesquisa("Pesquisa Teste", DATA_PADRAO)
                    .pergunta(1, "Faixa Etária",
                            resposta("a", "Entre 16 e 24 anos"),
                            resposta("b", "Entre 25 e 34 anos"),
                            resposta("c", "Entre 35 e 49 anos"),
                            resposta("d", "Entre 50 e 60 anos"),
                            resposta("e", "Acima de 60 anos")
                    )
                    .pergunta(2, "Sexo",
                            resposta("a", "M")
                    )
                    .pergunta(3, "Salário familiar",
                            resposta("a", "Até 1 salário mínimo"),
                            resposta("b", "Entre 1 e 3 salários mínimos"),
                            resposta("c", "Entre 4 e 7 salários mínimos"),
                            resposta("d", "Entre 8 e 10 salários mínimos"),
                            resposta("e", "Acima de 10 salários mínimos")
                    )
                    .folhas(3)
                    .check(estruturaBasica);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        String msgEsperada = "Resposta b da pergunta 2 não encontrada.";
        assertEquals(msgEsperada, msg);
    }

    private Pesquisa getEnvironmentDeTresFolhasDePesquisa() {
        Pesquisa pesquisa = pesquisaService.novo("PESQUISA TESTE", DATA_PADRAO);
        Pergunta pergunta1 = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta1, "a", "Entre 16 e 24 anos");
        respostaService.novo(pergunta1, "b", "Entre 25 e 34 anos");
        respostaService.novo(pergunta1, "c", "Entre 35 e 49 anos");
        respostaService.novo(pergunta1, "d", "Entre 50 e 60 anos");
        respostaService.novo(pergunta1, "e", "Acima de 60 anos");

        Pergunta pergunta2 = perguntaService.novo(pesquisa, 2, "Sexo");
        respostaService.novo(pergunta2, "a", "M");
        respostaService.novo(pergunta2, "b", "F");

        Pergunta pergunta3 = perguntaService.novo(pesquisa, 3, "Salário familiar");
        respostaService.novo(pergunta3, "a", "Até 1 salário mínimo");
        respostaService.novo(pergunta3, "b", "Entre 1 e 3 salários mínimos");
        respostaService.novo(pergunta3, "c", "Entre 4 e 7 salários mínimos");
        respostaService.novo(pergunta3, "d", "Entre 8 e 10 salários mínimos");
        respostaService.novo(pergunta3, "e", "Acima de 10 salários mínimos");

        folhaService.criarFolhas(pesquisa, 3);

        return pesquisa;
    }

}