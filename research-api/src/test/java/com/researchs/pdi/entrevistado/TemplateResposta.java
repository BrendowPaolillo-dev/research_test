package com.researchs.pdi.entrevistado;


import com.researchs.pdi.models.Resposta;

public class TemplateResposta {

    private Resposta resposta;

    public TemplateResposta resposta(String opcao, String descricao) {
        Resposta resposta = Resposta.builder()
                .opcao(opcao)
                .descricao(descricao)
                .build();

        this.resposta = resposta;
        return this;
    }

    public Resposta getResposta() {
        return resposta;
    }
}
