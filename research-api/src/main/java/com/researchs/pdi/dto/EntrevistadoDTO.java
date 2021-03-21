package com.researchs.pdi.dto;


import com.researchs.pdi.models.Folha;
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
public class EntrevistadoDTO {

    private Folha folha;
    private List<EntrevistadoPerguntaRespostaDTO> respostas;

}
