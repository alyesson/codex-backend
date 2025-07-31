package br.com.codex.v1.resources;

import br.com.codex.v1.domain.fiscal.NotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.service.NotaFiscalService;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.ServicosEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TRetInutNFe;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/nota_fiscal")
public class NotaFiscalResource {

    @Autowired
    private NotaFiscalService notaFiscalService;


    /**
     * Gera configuraçòes iniciais da nota fiscal.
     */
    @GetMapping("/status_servico/{cnpj}")
    public ResponseEntity<?> verificarStatusServico(@PathVariable String cnpj) {
        try {
            // Cria um DTO mínimo apenas com o CNPJ
            NotaFiscalDto dto = new NotaFiscalDto();
            dto.setDocumentoEmitente(cnpj);

            // Obtém as configurações
            ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);

            // Consulta o status
            TRetConsStatServ status = notaFiscalService.consultarStatusServico(config);

            // Retorna um objeto simplificado para o frontend
            Map<String, String> response = new HashMap<>();
            response.put("status", status.getCStat() + " - " + status.getXMotivo());
            response.put("dataHora", status.getDhRecbto() != null ? status.getDhRecbto().toString() : "");

            return ResponseEntity.ok(response);
        } catch (NfeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "Erro", "message", e.getMessage()));
        }
    }

    /**
     * Emite uma nova NF-e.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NotaFiscalDto> emitirNotaFiscal(@Valid @RequestBody NotaFiscalDto dto) throws Exception {
        NotaFiscalDto resultado = notaFiscalService.emitirNotaFiscal(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    /**
     * Visualiza as notas emitidas no mês corrente.
     */
    @GetMapping("/mes_corrente")
    public ResponseEntity<List<NotaFiscalDto>> consultarNotasMesCorrente(@RequestParam String documentoEmitente) {
        List<NotaFiscal> list = notaFiscalService.consultarNotasMesCorrente(documentoEmitente);
        List<NotaFiscalDto> listDto = list.stream().map(NotaFiscalDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * Consulta notas fiscais por período
     */
    @GetMapping("/por_periodo")
    public ResponseEntity<List<NotaFiscalDto>> consultarNotasPorPeriodo(@RequestParam String documentoEmitente,
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<NotaFiscal> list = notaFiscalService.consultarNotasPorPeriodo(documentoEmitente, dataInicial, dataFinal);
        List<NotaFiscalDto> listDto = list.stream().map(NotaFiscalDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    /**
     * Consulta o status de uma NF-e.
     */
    @GetMapping("/{chave}")
    public ResponseEntity<NotaFiscalDto> consultarNotaFiscal(@PathVariable String chave, @RequestParam String cnpj) throws Exception {

        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);

        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);
        br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TRetConsSitNFe retorno = notaFiscalService.consultarNotaFiscal(chave, config);

        NotaFiscalDto responseDto = new NotaFiscalDto();
        responseDto.setChave(chave);

        if (retorno.getProtNFe() != null && retorno.getProtNFe().getInfProt() != null) {
            responseDto.setCstat(retorno.getProtNFe().getInfProt().getCStat());
            responseDto.setMotivoProtocolo(retorno.getProtNFe().getInfProt().getXMotivo());
        } else {
            responseDto.setCstat(retorno.getCStat());
            responseDto.setMotivoProtocolo(retorno.getXMotivo());
        }
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Cancela uma NF-e.
     */
    @PostMapping("/{chave}/cancelar")
    public ResponseEntity<br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento> cancelarNotaFiscal(
            @PathVariable String chave, @RequestParam String protocolo, @RequestParam String motivo, @RequestParam String cnpj) throws Exception {

        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);
        br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento retorno = notaFiscalService.cancelarNotaFiscal(chave, protocolo, motivo, cnpj);

        return ResponseEntity.ok(retorno);
    }

    /**
     * Emite uma Carta de Correção para uma NF-e.
     */
    @PostMapping("/{chave}/carta_correcao")
    public ResponseEntity<TRetEnvEvento> cartaCorrecao(@PathVariable String chave, @RequestParam String correcao, @RequestParam String cnpj) throws Exception {
        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);
        TRetEnvEvento retorno = notaFiscalService.cartaCorrecao(chave, cnpj, correcao, config);
        return ResponseEntity.ok(retorno);
    }

    /**
     * Inutiliza uma faixa de numeração de NF-e.
     */
    @PostMapping("/inutilizar")
    public ResponseEntity<TRetInutNFe> inutilizarNotaFiscal(
            @RequestParam String cnpj,
            @RequestParam String justificativa,
            @RequestParam String ano,
            @RequestParam String serie,
            @RequestParam String numInicial,
            @RequestParam String numFinal) throws Exception {

        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);

        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);
        TRetInutNFe retorno = notaFiscalService.inutilizarNotaFiscal(cnpj, justificativa, ano, serie, numInicial, numFinal, config);
        return ResponseEntity.ok(retorno);
    }

    /**
     * Consulta o status de serviço da SEFAZ.
     */
    @GetMapping("/status")
    public ResponseEntity<TRetConsStatServ> consultarStatusServico(@RequestParam String cnpj) throws Exception {

        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);

        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);
        TRetConsStatServ retorno = notaFiscalService.consultarStatusServico(config);
        return ResponseEntity.ok(retorno);
    }

    /**
     * Envia um evento manual.
     */
    @PostMapping("/evento")
    public ResponseEntity<String> enviarEventoManual(
            @RequestBody String xmlEvento,
            @RequestParam String tipoEvento,
            @RequestParam boolean valida,
            @RequestParam boolean assina,
            @RequestParam String cnpj) throws Exception {

        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);

        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracoes(dto);
        String retorno = notaFiscalService.enviarEventoManual(xmlEvento,ServicosEnum.valueOf(tipoEvento), valida, assina, config);
        return ResponseEntity.ok(retorno);
    }

    /**
     * Consulta documentos fiscais por NSU.
     */
    @GetMapping("/distribuicao")
    public ResponseEntity<BigInteger> consultarDocumentos(
            @RequestParam String cnpj,
            @RequestParam String ambiente) throws Exception {

        BigInteger ultimoNsu = notaFiscalService.consultarNSU(cnpj, ambiente);
        return ResponseEntity.ok(ultimoNsu);
    }
}