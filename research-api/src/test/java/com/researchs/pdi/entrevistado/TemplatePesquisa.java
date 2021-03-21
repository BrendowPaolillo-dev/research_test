package com.researchs.pdi.entrevistado;

import com.researchs.pdi.dto.PerguntasERespostasDTO;
import com.researchs.pdi.dto.PesquisaDTO;
import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemplatePesquisa {

    private Pesquisa pesquisa;
    private List<PerguntasERespostasDTO> perguntasERespostasDTO;
    private int quantidadeEntrevistas;

    public static TemplateResposta resposta(String opcao, String descricao) {
        return new TemplateResposta().resposta(opcao, descricao);
    }

    public TemplatePesquisa pesquisa(String descricao, Date data) {
        Pesquisa pesquisa = new Pesquisa();
        pesquisa.setDescricao(descricao);
        pesquisa.setData(data);
        this.pesquisa = pesquisa;

        return this;
    }

    public TemplatePesquisa pergunta(Integer numero, String descricao, TemplateResposta... templateResposta) {
        getPerguntasERespostasDTO().add(setPerguntasERespostas(numero, descricao, templateResposta));
        return this;
    }

    private PerguntasERespostasDTO setPerguntasERespostas(Integer numero, String descricao, TemplateResposta[] templateResposta) {
        PerguntasERespostasDTO perguntaRespostas = new PerguntasERespostasDTO();
        perguntaRespostas.setPergunta(setPergunta(numero, descricao));
        perguntaRespostas.setRespostas(setRespostas(templateResposta));
        return perguntaRespostas;
    }

    private List<Resposta> setRespostas(TemplateResposta[] templateResposta) {
        List<Resposta> respostas = new ArrayList<Resposta>();
        for (TemplateResposta template : templateResposta) {
            respostas.add(template.getResposta());
        }
        return respostas;
    }

    private Pergunta setPergunta(Integer numero, String descricao) {
        Pergunta pergunta = Pergunta.builder()
                .pesquisa(pesquisa)
                .numero(numero)
                .descricao(descricao)
                .build();
        return pergunta;
    }

    public TemplatePesquisa folhas(int qtdeFolhas) {
        this.quantidadeEntrevistas = qtdeFolhas;
        return this;
    }

    private List<PerguntasERespostasDTO> getPerguntasERespostasDTO() {
        if (perguntasERespostasDTO == null) {
            perguntasERespostasDTO = new ArrayList<PerguntasERespostasDTO>();
        }
        return perguntasERespostasDTO;
    }

    public void check(PesquisaDTO estruturaBasica) {
        validarPesquisa(estruturaBasica.getPesquisa());
        validarPerguntasERespostas(estruturaBasica.getPerguntasERespostas());
        validarQuantidadeFolhas(estruturaBasica.getFolhas());
    }

    private void validarPesquisa(Pesquisa pesquisa) {
        if (!this.pesquisa.getDescricao().equalsIgnoreCase(pesquisa.getDescricao()) ||
            !this.pesquisa.getData().equals(pesquisa.getData()))
            throw new RuntimeException("Pesquisa Esperada:" + this.pesquisa.getDescricao() +
                    "\nPesquisa Retornada: " + pesquisa.getDescricao());
    }

    private void validarPerguntasERespostas(List<PerguntasERespostasDTO> perguntasERespostas) {
        for (PerguntasERespostasDTO perguntasEResposta : perguntasERespostas)
            valida(perguntasEResposta);

        for (PerguntasERespostasDTO perguntasEResposta : this.perguntasERespostasDTO) {
            Pergunta perguntaTemplate = perguntasEResposta.getPergunta();
            if (!achouPergunta(perguntasEResposta, perguntaTemplate, perguntasERespostas))
                throw new RuntimeException(getMessagePergunta(perguntaTemplate));
        }

    }

    private void valida(PerguntasERespostasDTO perguntasEResposta) {
        Pergunta perguntaEstruturaBasica = perguntasEResposta.getPergunta();
        if (!achouPergunta(perguntasEResposta, perguntaEstruturaBasica, this.getPerguntasERespostasDTO()))
            throw new RuntimeException(getMessagePergunta(perguntaEstruturaBasica));
    }

    private Boolean achouPergunta(PerguntasERespostasDTO origem, Pergunta destino, List<PerguntasERespostasDTO> alvo) {
        Boolean achou = false;
        for (PerguntasERespostasDTO template : alvo) {
            if (isMesmaPergunta(destino, template)) {
                achou = true;
                validaRespostas(origem, destino, template);
            }
        }
        return achou;
    }

    private void validaRespostas(PerguntasERespostasDTO perguntasEResposta, Pergunta perguntaEstruturaBasica, PerguntasERespostasDTO template) {
        for (Resposta respostaEstruturaBasica : perguntasEResposta.getRespostas()) {
            if (!achouResposta(template, respostaEstruturaBasica))
                throw new RuntimeException(getMessageResposta(perguntaEstruturaBasica, respostaEstruturaBasica));
        }
    }

    private Boolean achouResposta(PerguntasERespostasDTO template, Resposta respostaEstruturaBasica) {
        Boolean achouResposta = false;
        for (Resposta respostaTemplate : template.getRespostas()) {
            if (isMesmaResposta(respostaEstruturaBasica, respostaTemplate)) {
                achouResposta = true;
            }
        }
        return achouResposta;
    }

    private boolean isMesmaResposta(Resposta origem, Resposta destino) {
        return origem.getOpcao().equalsIgnoreCase(destino.getOpcao())
            && origem.getDescricao().equalsIgnoreCase(destino.getDescricao());
    }

    private String getMessagePergunta(Pergunta perguntaEstruturaBasica) {
        return "Pergunta " + perguntaEstruturaBasica.getDescricao() + " deveria existir.";
    }

    private String getMessageResposta(Pergunta perguntaEstruturaBasica, Resposta respostaEstruturaBasica) {
        return "Resposta " + respostaEstruturaBasica.getOpcao() + " da pergunta " + perguntaEstruturaBasica.getNumero() + " não encontrada.";
    }

    private boolean isMesmaPergunta(Pergunta origem, PerguntasERespostasDTO destino) {
        return origem.getDescricao().equalsIgnoreCase(destino.getPergunta().getDescricao())
            && origem.getNumero() == destino.getPergunta().getNumero();
    }

    private void validarQuantidadeFolhas(List<Folha> folhas) {
        if (folhas.size() != this.quantidadeEntrevistas)
            throw new RuntimeException(getMessageFolhas(folhas));
    }

    private String getMessageFolhas(List<Folha> folhas) {
        return "Quantidade de Folhas diferentes. Esperado: " + this.quantidadeEntrevistas + " - Atual: " + folhas.size();
    }
}
