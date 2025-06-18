package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.service.DistribuicaoDfeService;
import br.com.codex.v1.service.NotaFiscalService;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.DocumentoEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema_4.consSitNFe.TRetConsSitNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/api/nota_fiscal")
public class NotaFiscalResource {

    @Autowired
    private NotaFiscalService notaFiscalService;

    @Autowired
    private DistribuicaoDfeService distribuicaoDfeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotaFiscalDto emitirNotaFiscal(@Valid @RequestBody NotaFiscalDto dto) throws Exception {
        TEnviNFe enviNFe = notaFiscalService.montarNotaFiscal(dto);
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracao(dto);
        enviNFe = notaFiscalService.assinarNotaFiscal(enviNFe, config);
        notaFiscalService.validarNotaFiscal(enviNFe, config);
        TRetEnviNFe retorno = notaFiscalService.enviarNotaFiscal(enviNFe, config);

        dto.setChave(retorno.getProtNFe().getInfProt().getChNFe());
        dto.setCstat(retorno.getProtNFe().getInfProt().getCStat());
        dto.setNumeroProtocolo(retorno.getProtNFe().getInfProt().getNProt());

        String xml = XmlNfeUtil.objectToXml(enviNFe);
        notaFiscalService.salvarXmlNotaFiscal(dto.getChave(), xml);

        return dto;
    }

    @PostMapping("/distribuicao")
    public void consultarDocumentos(@RequestParam String cnpj, @RequestParam String ambiente) throws Exception {
        distribuicaoDfeService.consultarDocumentos(cnpj, ambiente);
    }

    @GetMapping("/{chave}")
    public NotaFiscalDto consultarNotaFiscal(@PathVariable String chave) throws Exception {
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracao(new NotaFiscalDto());
        TRetConsSitNFe retorno = Nfe.consultaNfe(config, chave, DocumentoEnum.NFE);
        // Mapear retorno para DTO
        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setChave(chave);
        dto.setCstat(retorno.getProtNFe().getInfProt().getCStat());
        dto.setMotivoProtocolo(retorno.getProtNFe().getInfProt().getXMotivo());
        return dto;
    }

    @PostMapping("/{chave}/cancelar")
    public void cancelarNotaFiscal(@PathVariable String chave, @RequestParam String protocolo,
                                   @RequestParam String motivo, @RequestParam String cnpj) throws Exception {
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracao(new NotaFiscalDto().setDocumentoEmitente(cnpj));
        notaFiscalService.cancelarNotaFiscal(chave, protocolo, motivo, cnpj, config);
    }
}
