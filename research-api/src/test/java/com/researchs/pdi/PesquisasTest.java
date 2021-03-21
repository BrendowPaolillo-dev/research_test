package com.researchs.pdi;

import com.researchs.pdi.config.FunctionalTest;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.PesquisaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.researchs.pdi.utils.DateUtils.getDate;
import static com.researchs.pdi.utils.DateUtils.getParse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FunctionalTest
class PesquisasTest {

	public static final Date DATA_PADRAO = getDate(getParse("01/11/2016"));

	private final PesquisaService pesquisaService;

	private Pesquisa novaPesquisa(String descricaoPesq, Date data) {
		return pesquisaService.novo(descricaoPesq, data);
	}

	@Autowired
	PesquisasTest(PesquisaService pesquisaService) {
		this.pesquisaService = pesquisaService;
	}

	@Test
	void deveExistirUmaPesquisa() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);

		List<Pesquisa> all = pesquisaService.pesquisa();

		assertEquals(1, all.size());
	}

	@Test
	void devemExistirDuasPesquisas() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);
		novaPesquisa("Pesquisa Mais um Teste", DATA_PADRAO);

		List<Pesquisa> all = pesquisaService.pesquisa();

		assertEquals(2, all.size());
	}

	@Test
	void retornarPesquisaTeste() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);
		novaPesquisa("Pesquisa Mais um Teste", DATA_PADRAO);

		List<Pesquisa> pesquisaTeste = pesquisaService.pesquisa("Pesquisa Teste");

		assertEquals(1, pesquisaTeste.size());
		assertEquals("Pesquisa Teste", pesquisaTeste.get(0).getDescricao());
	}

	@Test
	void atualizarPesquisa() {
		novaPesquisa("Pesquisa Teste",DATA_PADRAO);
		novaPesquisa("Pesquisa Mais um Teste", DATA_PADRAO);

		List<Pesquisa> pesquisaTeste = pesquisaService.pesquisa("Pesquisa Teste");

		Pesquisa pesquisaUpdate = pesquisaTeste.get(0);
		pesquisaUpdate.setDescricao("Pesquisa Alterada");
		pesquisaService.atualizar(pesquisaUpdate);

		pesquisaTeste = pesquisaService.pesquisa("Pesquisa Alterada");

		assertEquals(1, pesquisaTeste.size());
		assertEquals("Pesquisa Alterada", pesquisaTeste.get(0).getDescricao());
		assertEquals(2, pesquisaService.pesquisa().size());
	}

	@Test
	void naoPermiteCadastrarPesquisaMesmoNomeMesmaData() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);

		String erroMsg = null;
		try {
			novaPesquisa("Pesquisa Teste", DATA_PADRAO);
		}
		catch (RuntimeException e) {
			erroMsg = e.getMessage();
		}
		assertEquals("Pesquisa já cadastrada", erroMsg);

		List<Pesquisa> all = pesquisaService.pesquisa();
		assertEquals(1, all.size());
	}

	@Test
	void consultarPesquisaPelaData() {
		novaPesquisa("Pesquisa Setembro", getDate(getParse("30/09/2016")));
		novaPesquisa("Pesquisa Outubro", getDate(getParse("06/10/2016")));
		List<Pesquisa> outubro1 = pesquisaService.pesquisa(getDate(getParse("01/10/2016")));
		assertTrue(outubro1.isEmpty());

		List<Pesquisa> setembro30 = pesquisaService.pesquisa(getDate(getParse("30/09/2016")));
		assertEquals(1, setembro30.size());
		assertEquals(getDate(getParse("30/09/2016")), setembro30.get(0).getData());
	}

	@Test
	void naoPermiteAtualizarPesquisaMesmoNomeMesmaData() {
		novaPesquisa("Pesquisa Teste 1", DATA_PADRAO);
		Pesquisa pesquisaTeste2 = novaPesquisa("Pesquisa Teste 2", DATA_PADRAO);

		String erroMsg = null;
		try {
			pesquisaService.atualizar(pesquisaTeste2, "Pesquisa Teste 1", pesquisaTeste2.getData());
		}
		catch (RuntimeException e) {
			erroMsg = e.getMessage();
		}
		assertEquals("Pesquisa já cadastrada", erroMsg);

		List<Pesquisa> all = pesquisaService.pesquisa();
		assertEquals(2, all.size());
	}

	@Test
	void devePermitirAtualizarPesquisaComMesmosDados() {
		novaPesquisa("Pesquisa Teste 1", DATA_PADRAO);
		Pesquisa pesquisaTeste2 = novaPesquisa("Pesquisa Teste 2", DATA_PADRAO);

		pesquisaService.atualizar(pesquisaTeste2, "Pesquisa Teste 2", pesquisaTeste2.getData());

		List<Pesquisa> all = pesquisaService.pesquisa();
		assertEquals(2, all.size());
	}

}
