package com.researchs.pdi.dto;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Resposta;
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
public class PerguntasERespostasDTO {

    private Pergunta pergunta;
    private List<Resposta> respostas;

}
