package com.researchs.pdi.dto;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Resposta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EntrevistadoPerguntaRespostaDTO {

    private Pergunta pergunta;
    private Resposta resposta;

}
