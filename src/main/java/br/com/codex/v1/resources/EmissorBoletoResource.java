package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.EmissorBoletoDto;
import br.com.codex.v1.domain.financeiro.EmissorBoleto;
import br.com.codex.v1.service.EmissorBoletoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/emissao_boleto")
public class EmissorBoletoResource {
    
    @Autowired
    private EmissorBoletoService emissorBoletoService;

        @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
        @PostMapping("/emitir")
        public ResponseEntity<byte[]> emitirBoleto(@Valid @RequestBody EmissorBoletoDto emissorBoletoDto) {
            try {
                // 1. Gera o PDF e salva os metadados
                File pdfBoleto = emissorBoletoService.emitirBoleto(emissorBoletoDto);

                // 2. Converte o PDF para byte[]
                byte[] pdfBytes = Files.readAllBytes(pdfBoleto.toPath());

                // 3. Configura os headers para download
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                String nomeArquivo = "boleto_" + emissorBoletoDto.getNumeroDocumento() + ".pdf";
                headers.setContentDisposition(ContentDisposition.builder("attachment").filename(nomeArquivo).build());

                // 4. Retorna o PDF para download
                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

            } catch (ParseException e) {
                return ResponseEntity.badRequest().body(("Erro de formatação: " + e.getMessage()).getBytes());
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(("Erro ao gerar PDF: " + e.getMessage()).getBytes());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(("Erro no processamento: " + e.getMessage()).getBytes());
            }
        }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @PostMapping
    public ResponseEntity< EmissorBoletoDto> create(@Valid @RequestBody  EmissorBoletoDto emissorBoletoDto){
        EmissorBoleto obj = emissorBoletoService.create(emissorBoletoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmissorBoletoDto> findById(@PathVariable Long id){
         EmissorBoleto objGrupo = emissorBoletoService.findById(id);
        return ResponseEntity.ok().body(new  EmissorBoletoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping(value = "/boletos_periodo")
    public ResponseEntity<List< EmissorBoletoDto>>findAllAtendimentosPeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                          @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List< EmissorBoleto> list = emissorBoletoService.findAllBoletosPeriodo(dataInicial, dataFinal);
        List< EmissorBoletoDto> listDto = list.stream().map(EmissorBoletoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
