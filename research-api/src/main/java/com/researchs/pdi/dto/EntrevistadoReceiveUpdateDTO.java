package com.researchs.pdi.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EntrevistadoReceiveUpdateDTO extends EntrevistadoReceiveDTO {

    private Integer id;

}
