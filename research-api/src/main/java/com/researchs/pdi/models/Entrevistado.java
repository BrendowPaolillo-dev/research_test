package com.researchs.pdi.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ENTREVISTADO",
        indexes = {
                @Index(
                        name = "IX_ENTREVISTADO",
                        columnList = "pesquisa, folha, pergunta",
                        unique = true
                )
        }
)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Entrevistado {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PESQUISA")
    private Pesquisa pesquisa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FOLHA")
    private Folha folha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERGUNTA")
    private Pergunta pergunta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESPOSTA")
    private Resposta resposta;

}
