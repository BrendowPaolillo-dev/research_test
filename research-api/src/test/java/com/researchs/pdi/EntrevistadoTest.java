package com.researchs.pdi;

import com.researchs.pdi.config.FunctionalTest;
import com.researchs.pdi.dto.EntrevistadoDTO;
import com.researchs.pdi.dto.EntrevistadoPerguntaRespostaDTO;
import com.researchs.pdi.dto.EntrevistadoReceiveDTO;
import com.researchs.pdi.dto.EntrevistadoReceiveUpdateDTO;
import com.researchs.pdi.models.Entrevistado;
import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import com.researchs.pdi.rest.SendPesquisaController;
import com.researchs.pdi.services.EntrevistadoService;
import com.researchs.pdi.services.FolhaService;
import com.researchs.pdi.services.PerguntaService;
import com.researchs.pdi.services.PesquisaService;
import com.researchs.pdi.services.RespostaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.researchs.pdi.utils.DateUtils.getDate;
import static com.researchs.pdi.utils.DateUtils.getParse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FunctionalTest
class EntrevistadoTest {

    public static final Date DATA_PADRAO = getDate(getParse("01/11/2016"));

    private final PesquisaService pesquisaService;
    private final PerguntaService perguntaService;
    private final RespostaService respostaService;
    private final FolhaService folhaService;
    private final EntrevistadoService entrevistadoService;
    private final SendPesquisaController sendPesquisaController;

    @Autowired
    EntrevistadoTest(PesquisaService pesquisaService,
                     PerguntaService perguntaService,
                     RespostaService respostaService,
                     FolhaService folhaService,
                     EntrevistadoService entrevistadoService,
                     SendPesquisaController sendPesquisaController) {
        this.pesquisaService = pesquisaService;
        this.perguntaService = perguntaService;
        this.respostaService = respostaService;
        this.folhaService = folhaService;
        this.entrevistadoService = entrevistadoService;
        this.sendPesquisaController = sendPesquisaController;
    }

    @Test
    void deveriaTerUmaEntrevistaCadastrada() {
        Pesquisa pesquisa = getEnvironmentDeTresFolhasDePesquisa();

        for (Folha folha: folhaService.pesquisa(pesquisa))
            entrevistadoService.novo(pesquisa, montaRespostas(folha));

        List<Entrevistado> entrevistadoFolha1 = entrevistadoService.pesquisa(pesquisa, folhaService.pesquisa(pesquisa, 1));
        List<Entrevistado> entrevistadoFolha2 = entrevistadoService.pesquisa(pesquisa, folhaService.pesquisa(pesquisa, 2));
        List<Entrevistado> entrevistadoFolha3 = entrevistadoService.pesquisa(pesquisa, folhaService.pesquisa(pesquisa, 3));

        assertEquals(3, folhaService.pesquisa(pesquisa).size(), "Deveriam ter 3 folhas cadastradas");
        assertEquals(3, entrevistadoFolha1.size(), "Deveriam ter 3 respostas para a folha 1");
        assertEquals(3, entrevistadoFolha2.size(), "Deveriam ter 3 respostas para a folha 2");
        assertEquals(3, entrevistadoFolha3.size(), "Deveriam ter 3 respostas para a folha 3");
        assertEquals(9, entrevistadoService.pesquisa(pesquisa).size(), "Deveriam ter 9 registros de entrevistados");
    }

    @Test
    void validacaoDeFolhaPesquisaEOpcao() {
        Pesquisa pesquisa = getEnvironmentDeTresFolhasDePesquisa();

        for (Folha folha: folhaService.pesquisa(pesquisa))
            entrevistadoService.novo(pesquisa, montaRespostas(folha));

        Folha folha1 = folhaService.pesquisa(pesquisa, 1);
        Folha folha2 = folhaService.pesquisa(pesquisa, 2);
        Folha folha3 = folhaService.pesquisa(pesquisa, 3);

        List<Entrevistado> entrevistadoFolha1 = entrevistadoService.pesquisa(pesquisa, folha1);
        List<Entrevistado> entrevistadoFolha2 = entrevistadoService.pesquisa(pesquisa, folha2);
        List<Entrevistado> entrevistadoFolha3 = entrevistadoService.pesquisa(pesquisa, folha3);

        for (Entrevistado entrevistado: entrevistadoFolha1) {
            assertEquals(folha1, entrevistado.getFolha());
            assertEquals(pesquisa, entrevistado.getPesquisa());
            assertEquals("a", entrevistado.getResposta().getOpcao(), "Resposta opção A");
        }

        for (Entrevistado entrevistado: entrevistadoFolha2) {
            assertEquals(folha2, entrevistado.getFolha());
            assertEquals(pesquisa, entrevistado.getPesquisa());
            assertEquals("a", entrevistado.getResposta().getOpcao(), "Resposta opção A");
        }

        for (Entrevistado entrevistado: entrevistadoFolha3) {
            assertEquals(folha3, entrevistado.getFolha());
            assertEquals(pesquisa, entrevistado.getPesquisa());
            assertEquals("a", entrevistado.getResposta().getOpcao(), "Resposta opção A");
        }
    }

    @Test
    void naoDeveAceitarFolhaRespondidaIncompleta() {
        Pesquisa pesquisa = getEnvironmentDeTresFolhasDePesquisa();
        List<EntrevistadoPerguntaRespostaDTO> respostas = new ArrayList<>();

        EntrevistadoPerguntaRespostaDTO e1 = new EntrevistadoPerguntaRespostaDTO();
        e1.setPergunta(perguntaService.pesquisa(pesquisa, 1));
        e1.setResposta(respostaService.pesquisa(pesquisa, 1, "a"));

        EntrevistadoPerguntaRespostaDTO e2 = new EntrevistadoPerguntaRespostaDTO();
        e2.setPergunta(perguntaService.pesquisa(pesquisa, 2));
        e2.setResposta(respostaService.pesquisa(pesquisa, 2, "a"));

        respostas.add(e1);
        respostas.add(e2);

        EntrevistadoDTO entrevistadoDTO = new EntrevistadoDTO();
        entrevistadoDTO.setFolha(folhaService.pesquisa(pesquisa, 1));
        entrevistadoDTO.setRespostas(respostas);

        String msg = null;
        try {
            entrevistadoService.novo(pesquisa, entrevistadoDTO);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        assertNotNull(msg);
    }

    @Test
    void deveSalvarEntrevistaExterna() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getId();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveUpdateDTO folha1Pergunta1 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setOpcaoResposta(pergunta1RespostaA.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta2 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setOpcaoResposta(pergunta2RespostaB.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta3 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setOpcaoResposta(pergunta3RespostaC.getOpcao());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisaController.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        assertNotNull(erro);

        List<Entrevistado> entrevistaFolha1 = entrevistadoService.pesquisa(pesquisaTeste, folhaService.pesquisa(folha1));
        for (Entrevistado entrevistado : entrevistaFolha1) {
            deveHaverUmaOpcaoPergunta(entrevistado, pergunta1, pergunta2, pergunta3);
        }
    }

    @Test
    void pesquisaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getId();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveUpdateDTO folha1Pergunta1 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(10);
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setOpcaoResposta(pergunta1RespostaA.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta2 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setOpcaoResposta(pergunta2RespostaB.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta3 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setOpcaoResposta(pergunta3RespostaC.getOpcao());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisaController.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        assertNotNull(erro);
        assertEquals("Pesquisa não encontrada", erro,"Pesquisa não encontrada");
    }

    @Test
    void folhaPesquisaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveUpdateDTO folha1Pergunta1 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(1001);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setOpcaoResposta(pergunta1RespostaA.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta2 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(1001);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setOpcaoResposta(pergunta2RespostaB.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta3 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(1001);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setOpcaoResposta(pergunta3RespostaC.getOpcao());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisaController.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        assertEquals("Folha de Pesquisa não encontrada", erro, "Folha de Pesquisa não encontrada");
    }

    @Test
    void perguntaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getNumero();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveUpdateDTO folha1Pergunta1 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(1001);
        folha1Pergunta1.setOpcaoResposta(pergunta1RespostaA.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta2 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(1002);
        folha1Pergunta2.setOpcaoResposta(pergunta2RespostaB.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta3 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(1003);
        folha1Pergunta3.setOpcaoResposta(pergunta3RespostaC.getOpcao());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisaController.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        assertEquals("Pergunta não encontrada", erro, "Pergunta não encontrada");
    }

    @Test
    void respostaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getNumero();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveUpdateDTO folha1Pergunta1 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setOpcaoResposta("h");

        EntrevistadoReceiveUpdateDTO folha1Pergunta2 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setOpcaoResposta(pergunta2RespostaB.getOpcao());

        EntrevistadoReceiveUpdateDTO folha1Pergunta3 = new EntrevistadoReceiveUpdateDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setOpcaoResposta(pergunta3RespostaC.getOpcao());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisaController.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        assertEquals("Pergunta não encontrada", erro, "Resposta não encontrada");
    }

    private void deveHaverUmaOpcaoPergunta(Entrevistado entrevistado, Pergunta pergunta1, Pergunta pergunta2, Pergunta pergunta3) {
        boolean achou = false;

        if (entrevistado.getPergunta() == pergunta1)
            achou = true;
        if (entrevistado.getPergunta() == pergunta2)
            achou = true;
        if (entrevistado.getPergunta() == pergunta3)
            achou = true;

        assertTrue(achou);
    }

    private Pesquisa getEnvironmentDeTresFolhasDePesquisa() {
        Pesquisa pesquisa = pesquisaService.novo("Teste", DATA_PADRAO);
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

    private EntrevistadoDTO montaRespostas(Folha folha) {
        Pesquisa pesquisa = pesquisaService.pesquisa("Teste", DATA_PADRAO);

        List<EntrevistadoPerguntaRespostaDTO> respostas = new ArrayList<>();

        EntrevistadoPerguntaRespostaDTO e1 = new EntrevistadoPerguntaRespostaDTO();
        e1.setPergunta(perguntaService.pesquisa(pesquisa, 1));
        e1.setResposta(respostaService.pesquisa(pesquisa, 1, "a"));

        EntrevistadoPerguntaRespostaDTO e2 = new EntrevistadoPerguntaRespostaDTO();
        e2.setPergunta(perguntaService.pesquisa(pesquisa, 2));
        e2.setResposta(respostaService.pesquisa(pesquisa, 2, "a"));

        EntrevistadoPerguntaRespostaDTO e3 = new EntrevistadoPerguntaRespostaDTO();
        e3.setPergunta(perguntaService.pesquisa(pesquisa, 3));
        e3.setResposta(respostaService.pesquisa(pesquisa, 3, "a"));

        respostas.add(e1);
        respostas.add(e2);
        respostas.add(e3);

        EntrevistadoDTO entrevistadoDTO = new EntrevistadoDTO();
        entrevistadoDTO.setFolha(folha);
        entrevistadoDTO.setRespostas(respostas);
        return entrevistadoDTO;
    }

}
