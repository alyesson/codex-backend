package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ProdutoDto;
import br.com.codex.v1.domain.estoque.NotasFiscais;
import br.com.codex.v1.domain.dto.NotasFiscaisDto;
import br.com.codex.v1.domain.repository.NotaFiscalRepository;
import br.com.codex.v1.resources.exceptions.StandardError;
import br.com.codex.v1.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/nota_fiscal")
public class NotaFiscalResource {

    @Autowired
    private NotaFiscalService notaFiscalService;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    URI uri = null;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE', 'ROLE_GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping("/xml_nota")
    public ResponseEntity<NotasFiscaisDto> create(@RequestParam("files") MultipartFile[] files) throws Exception {

        for (MultipartFile file : files) {
            NotasFiscais objNota = notaFiscalService.obterXmlCompleto(file);
            uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objNota.getId()).toUri();
        }
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NotasFiscaisDto> delete(@PathVariable Integer id){
        notaFiscalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Mostra Todas as Notas Fiscais do Ano Corrente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ROLE_GERENTE_ADMINISTRATIVO','ADMINISTRATIVO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/ano_corrente")
    public ResponseEntity<List<NotasFiscaisDto>> findAllByYear(@RequestParam(value = "ano") Integer ano){
        List<NotasFiscais> list = notaFiscalService.findAllByYear(ano);
        List<NotasFiscaisDto> objList = list.stream().map(NotasFiscaisDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objList);
    }

    //Entrada de Nota Fiscal Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ROLE_GERENTE_ADMINISTRATIVO','ADMINISTRATIVO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/entrada_notas_fiscais_periodo")
    public ResponseEntity<List<NotasFiscaisDto>> findAllEntradaPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<NotasFiscais> list = notaFiscalService.findAllEntradaPeriodo(dataInicial, dataFinal);
        List<NotasFiscaisDto> listDto = list.stream().map(NotasFiscaisDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Pesquisa Nota Fiscal Por Meio do Número e Do Emitente (razão Social)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ROLE_GERENTE_ADMINISTRATIVO','ADMINISTRATIVO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/id_nota_emissor")
    public ResponseEntity<NotasFiscaisDto> findNotaFiscalByIdAndEmissor(@RequestParam("numero") String numero,@RequestParam("razaoSocialEmitente") String razaoSocialEmitente) {
        NotasFiscais objNotas = notaFiscalService.findNotaFiscalByIdAndEmissor(numero, razaoSocialEmitente);
        return ResponseEntity.ok().body(new NotasFiscaisDto(objNotas));
    }
}
