package br.com.codex.v1.resources;

import br.com.codex.v1.domain.vendas.Venda;
import br.com.codex.v1.domain.dto.VendaDto;
import br.com.codex.v1.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/vendas")
public class VendaResource {

    @Autowired
    private VendaService vendaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @PostMapping
    public ResponseEntity<VendaDto> create(@RequestBody VendaDto vendaDto){
        Venda objVenda = vendaService.create(vendaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objVenda.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaDto> findById(@PathVariable Integer id){
        Venda objVenda = vendaService.findById(id);
        return ResponseEntity.ok().body(new VendaDto(objVenda));
    }

    //Neste método serão listadas todas as vendas no ano ordenando do mais recente para o mais antigo
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping("/vendas_ano")
    public ResponseEntity <List<VendaDto>> findAllByYear(@RequestParam(value = "ano") Integer ano){
        List<Venda> objVenda = vendaService.findAllByYear(ano);
        List<VendaDto> listDto = objVenda.stream().map(VendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Contabiliza as vendas no mês - calcula quanto em dinheiro recebeu
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/vendas_mes")
    public ResponseEntity<List<VendaDto>> findAllMonth(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "mes") Integer mes){
        List<Venda> list = vendaService.findAllByYearAndMonth(ano, mes);
        List<VendaDto> listDto = list.stream().map(VendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Conta a quantidade de vendas realziadas no mês
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/vendas_contabilizadas_mes")
    public ResponseEntity<Integer> contaVendasMes(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "mes") Integer mes){
        int totalVendido = vendaService.contaVendasMes(ano, mes);
        return ResponseEntity.ok(totalVendido);
    }

    //Venda por período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/vendas_periodo")
    public ResponseEntity<List<VendaDto>> findAllVendaPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<Venda> list = vendaService.findAllVendaPeriodo(dataInicial, dataFinal);
        List<VendaDto> listDto = list.stream().map(VendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Contabiliza os melhores vendedores
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/melhores_vendedores")
    public ResponseEntity<List<VendaDto>> findAllVendedoresPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<Object[]> resultados = vendaService.findVendedoresByNumeroVendas(dataInicial, dataFinal);

        // Mapeia cada resultado para um objeto VendaDto
        List<VendaDto> listDto = resultados.stream()
                .map(result -> {
                    VendaDto dto = new VendaDto();
                    dto.setVendedor((String) result[0]); // Nome do vendedor na primeira posição
                    dto.setQuantidade(((Number) result[1]).intValue());
                    dto.setTotalVenda((BigDecimal) result[2]); // Total de vendas na terceira posição
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Contabiliza os melhores clientes
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/melhores_clientes")
    public ResponseEntity<List<VendaDto>> findByVendasClientes(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<Object[]> resultados = vendaService.findByVendasClientes(dataInicial, dataFinal);

        // Mapeia cada resultado para um objeto VendaDto
        List<VendaDto> listDto = resultados.stream()
                .map(result -> {
                    VendaDto dto = new VendaDto();
                    dto.setCliente((String) result[0]); // Nome do vendedor na primeira posição
                    dto.setTotalVenda((BigDecimal) result[1]); // Total de vendas na terceira posição
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
