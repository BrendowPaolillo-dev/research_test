package com.researchs.pdi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EntrevistadoReceiveDTO {

    @ApiModelProperty("ID da Pesquisa")
    @NotNull
    private Integer pesquisa;

    @ApiModelProperty("Número da Folha")
    @NotNull
    private Integer folha;

    @ApiModelProperty("Número da Pergunta")
    @NotNull
    private Integer pergunta;

    @ApiModelProperty("Opção da Resposta")
    @NotNull
    private String opcaoResposta;

}
