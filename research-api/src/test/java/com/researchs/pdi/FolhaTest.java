package com.researchs.pdi;

import com.researchs.pdi.config.FunctionalTest;
import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.FolhaService;
import com.researchs.pdi.services.PesquisaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@FunctionalTest
class FolhaTest {

    private final PesquisaService pesquisaService;
    private final FolhaService folhaService;

    @Autowired
    FolhaTest(PesquisaService pesquisaService,
              FolhaService folhaService) {
        this.pesquisaService = pesquisaService;
        this.folhaService = folhaService;
    }

    @Test
    void deveriaTerUmaFolhaCadastrada() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.novo(pesquisa, 1);

        assertEquals(1, folhaService.pesquisa().size());
    }

    @Test
    void deveriamTerDuasFolhasCadastradas() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.novo(pesquisa, 1);
        folhaService.novo(pesquisa, 2);

        assertEquals(2, folhaService.pesquisa().size());
    }

    @Test
    void deveriamTerDuasFolhasCadastradasUmaParaCadaPesquisa() {
        Pesquisa pesquisaTeste1 = pesquisaService.novo("Pesquisa Teste 1", new Date());
        Pesquisa pesquisaTeste2 = pesquisaService.novo("Pesquisa Teste 2", new Date());

        folhaService.novo(pesquisaTeste1, 1);
        folhaService.novo(pesquisaTeste2, 1);

        assertEquals(2, folhaService.pesquisa().size());
        assertEquals(1, folhaService.pesquisa(pesquisaTeste1).size());
        assertEquals(1, folhaService.pesquisa(pesquisaTeste2).size());
    }

    @Test
    void naoDevePermitirMesmoNumeroDeFolhaParaMesmaPesquisa() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.novo(pesquisa, 1);
        String msg = null;
        try {
            folhaService.novo(pesquisa, 1);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }
        assertNotNull(msg);
    }

    @Test
    void deveriaCriar10FolhasDePesquisaMostrandoMenorEMaiorNumeroDeFolha() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.criarFolhas(pesquisa, 10);
        assertEquals(10, folhaService.pesquisa(pesquisa).size(), "Foram criadas 10 folhas");
        assertEquals(Integer.valueOf(1), folhaService.min(pesquisa), "Menor Numero Folha");
        assertEquals(Integer.valueOf(10), folhaService.max(pesquisa), "Maior Numero Folha");

        Folha folha6 = folhaService.pesquisa(pesquisa, 6);
        assertEquals(Integer.valueOf(6), folha6.getNumero(), "Folha 6");
        assertEquals(pesquisa, folha6.getPesquisa(), "Pesquisa Teste");
    }

    @Test
    void deveriaCriar5FolhasParaPesquisaUmE2FolhasParaPesquisa2MostrandoMenorEMaiorNumeroDeFolha() {
        Pesquisa pesquisaUm = pesquisaService.novo("Pesquisa Um", new Date());
        Pesquisa pesquisaDois = pesquisaService.novo("Pesquisa Dois", new Date());

        folhaService.criarFolhas(pesquisaUm, 5);
        folhaService.criarFolhas(pesquisaDois, 2);

        assertEquals(5, folhaService.pesquisa(pesquisaUm).size());
        assertEquals(Integer.valueOf(1), folhaService.min(pesquisaUm));
        assertEquals(Integer.valueOf(5), folhaService.max(pesquisaUm));

        assertEquals(2, folhaService.pesquisa(pesquisaDois).size());
        assertEquals(Integer.valueOf(1), folhaService.min(pesquisaDois));
        assertEquals(Integer.valueOf(2), folhaService.max(pesquisaDois));
    }

}
