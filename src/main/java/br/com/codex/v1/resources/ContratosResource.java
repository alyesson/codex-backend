package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.Contratos;
import br.com.codex.v1.domain.dto.ContratosDto;
import br.com.codex.v1.service.ContratosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/contratos")
public class ContratosResource {

    @Autowired
    private ContratosService contratosService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ContratosDto> findById(@PathVariable Integer id) {
        Contratos obj = contratosService.findById(id);
        return ResponseEntity.ok().body(new ContratosDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping
    public ResponseEntity<List<ContratosDto>> findAll() {
        List<Contratos> list = contratosService.findAll();
        List<ContratosDto> listDto = list.stream().map(ContratosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestParam("nomeContrato") String nomeContrato,
                                         @RequestParam("inicioContrato") Date inicioContrato,
                                         @RequestParam("terminoContrato") Date terminoContrato,
                                         @RequestParam("tipoPessoa") String tipoPessoa,
                                         @RequestParam("numeroCnpj") String numeroCnpj,
                                         @RequestParam("relacao") String relacao,
                                         @RequestParam("razaoSocial") String razaoSocial,
                                         @RequestParam("diaVenceParcela") String diaVenceParcela,
                                         @RequestParam("tipoContrato") String tipoContrato,
                                         @RequestParam("valorContrato") String valorContrato,
                                         @RequestParam("pis") String pis,
                                         @RequestParam("ipi") String ipi,
                                         @RequestParam("icms") String icms,
                                         @RequestParam("cofins") String cofins,
                                         @RequestParam("iss") String iss,
                                         @RequestParam("frete") String frete,
                                         @RequestParam("valorDesconto") String valorDesconto,
                                         @RequestParam("valorLiquido") String valorLiquido,
                                         @RequestParam("renegociado") String renegociado,
                                         @RequestParam("dataRenegociacao") String dataRenegociacao,
                                         @RequestParam("observacao") String observacao,
                                         @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {

        try {
            // Cria um objeto Documento com os dados do formulário
            ContratosDto documento = new ContratosDto();
            documento.setNomeContrato(nomeContrato);
            documento.setInicioContrato(inicioContrato);
            documento.setTerminoContrato(terminoContrato);
            documento.setTipoPessoa(tipoPessoa);
            documento.setNumeroCnpj(numeroCnpj);
            documento.setRelacao(relacao);
            documento.setRazaoSocial(razaoSocial);
            documento.setDiaVenceParcela(diaVenceParcela);
            documento.setTipoContrato(tipoContrato);
            documento.setValorContrato(new BigDecimal(valorContrato.replace(",", ".")));
            documento.setPis(new BigDecimal(pis.replace(",", ".")));
            documento.setIpi(new BigDecimal(ipi.replace(",", ".")));
            documento.setIcms(new BigDecimal(icms.replace(",", ".")));
            documento.setCofins(new BigDecimal(cofins.replace(",", ".")));
            documento.setIss(new BigDecimal(iss.replace(",", ".")));
            documento.setFrete(new BigDecimal(frete.replace(",", ".")));
            documento.setValorDesconto(new BigDecimal(valorDesconto.replace(",", ".")));
            documento.setValorLiquido(new BigDecimal(valorLiquido.replace(",", ".")));
            documento.setRenegociado(renegociado);
            documento.setDataRenegociacao(dataRenegociacao);
            documento.setObservacao(observacao);

            if (arquivo != null) {
                documento.setArquivo(arquivo.getBytes());
            }else{
                documento.setArquivo(null);
            }

            Contratos objDoc = contratosService.create(documento);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDoc.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar contrato: "+e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Integer id,
                                         @RequestParam("nomeContrato") String nomeContrato,
                                         @RequestParam("inicioContrato") Date inicioContrato,
                                         @RequestParam("terminoContrato") Date terminoContrato,
                                         @RequestParam("tipoPessoa") String tipoPessoa,
                                         @RequestParam("numeroCnpj") String numeroCnpj,
                                         @RequestParam("relacao") String relacao,
                                         @RequestParam("razaoSocial") String razaoSocial,
                                         @RequestParam("diaVenceParcela") String diaVenceParcela,
                                         @RequestParam("tipoContrato") String tipoContrato,
                                         @RequestParam("valorContrato") BigDecimal valorContrato,
                                         @RequestParam("pis") BigDecimal pis,
                                         @RequestParam("ipi") BigDecimal ipi,
                                         @RequestParam("icms") BigDecimal icms,
                                         @RequestParam("cofins") BigDecimal cofins,
                                         @RequestParam("iss") BigDecimal iss,
                                         @RequestParam("frete") BigDecimal frete,
                                         @RequestParam("valorDesconto") BigDecimal valorDesconto,
                                         @RequestParam("valorLiquido") BigDecimal valorLiquido,
                                         @RequestParam("renegociado") String renegociado,
                                         @RequestParam("dataRenegociacao") String dataRenegociacao,
                                         @RequestParam("observacao") String observacao,
                                         @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {

        try {
            // Cria um objeto Documento com os dados do formulário
            ContratosDto documento = new ContratosDto();
            documento.setId(id);
            documento.setNomeContrato(nomeContrato);
            documento.setInicioContrato(inicioContrato);
            documento.setTerminoContrato(terminoContrato);
            documento.setTipoPessoa(tipoPessoa);
            documento.setNumeroCnpj(numeroCnpj);
            documento.setRelacao(relacao);
            documento.setRazaoSocial(razaoSocial);
            documento.setDiaVenceParcela(diaVenceParcela);
            documento.setTipoContrato(tipoContrato);
            documento.setValorContrato(valorContrato);
            documento.setPis(pis);
            documento.setIpi(ipi);
            documento.setIcms(icms);
            documento.setCofins(cofins);
            documento.setIss(iss);
            documento.setFrete(frete);
            documento.setValorDesconto(valorDesconto);
            documento.setValorLiquido(valorLiquido);
            documento.setRenegociado(renegociado);
            documento.setDataRenegociacao(dataRenegociacao);
            documento.setObservacao(observacao);

            if (arquivo != null) {
                documento.setArquivo(arquivo.getBytes());
            }else{
                documento.setArquivo(null);
            }

            contratosService.update(documento);
            return ResponseEntity.ok().body("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar contrato: "+e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        contratosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/downloads/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Integer id) {
        Contratos objDoc = contratosService.findById(id);
        byte[] fileBytes = objDoc.getArquivo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "currículo_"+objDoc.getNomeContrato());
        headers.setContentLength(fileBytes.length);

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}
