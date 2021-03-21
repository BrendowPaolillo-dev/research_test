package com.researchs.pdi.rest;

import com.researchs.pdi.dto.EntrevistadoReceiveDTO;
import com.researchs.pdi.dto.PesquisaDTO;
import com.researchs.pdi.http.SendPesquisaGateway;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/dados")
public class SendPesquisaController {

    private final SendPesquisaGateway gateway;

    public SendPesquisaController(SendPesquisaGateway gateway) {
        this.gateway = gateway;
    }

    @ApiOperation("Consulta de Dados")
    @GetMapping("/estrutura-basica")
    public PesquisaDTO estruturaBasica(HttpServletRequest request) {
        return gateway.getEstruturaBasica(request);
    }

    @ApiOperation("Este é o Primeiro Passo para testar sua App: necessário Fazer Login Antes de Iniciar")
    @PostMapping("/initDB")
    public void initDB(HttpServletRequest request) {
        gateway.initDB(request);
    }

    @ApiOperation("Aqui deve-se enviar a Folha de Resposta Toda Preenchida")
    @PostMapping("/enviar-entrevistados")
    public void enviarEntrevistados(
            HttpServletRequest request,
            @Validated
            @RequestBody List<EntrevistadoReceiveDTO> comando) throws Exception {

        for (EntrevistadoReceiveDTO dto : Collections.unmodifiableList(comando))
            gateway.salvar(dto);
    }

}
