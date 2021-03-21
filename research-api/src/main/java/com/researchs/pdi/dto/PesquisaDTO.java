package com.researchs.pdi.dto;


import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PesquisaDTO {

    private Pesquisa pesquisa;
    private List<PerguntasERespostasDTO> perguntasERespostas;
    private List<Folha> folhas;

}
