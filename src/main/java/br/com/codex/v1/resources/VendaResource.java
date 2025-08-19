package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.OrcamentoDto;
import br.com.codex.v1.domain.dto.OrcamentoItensDto;
import br.com.codex.v1.domain.dto.VendaDto;
import br.com.codex.v1.domain.dto.VendaItensDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.OrcamentoItens;
import br.com.codex.v1.domain.vendas.Venda;
import br.com.codex.v1.domain.vendas.VendaItens;
import br.com.codex.v1.service.OrcamentoService;
import br.com.codex.v1.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/vendas")
public class VendaResource {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private OrcamentoService orcamentoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @PostMapping
    public ResponseEntity<VendaDto> create(@RequestBody VendaDto vendaDto){
        Venda objVenda = vendaService.create(vendaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objVenda.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    //Neste métudo serão listadas todas as vendas no ano ordenando do mais recente para o mais antigo
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
    public ResponseEntity<List<VendaDto>> findAllVendaPeriodo(@RequestParam("dataInicial") LocalDate dataInicial, @RequestParam("dataFinal") LocalDate dataFinal){
        List<Venda> list = vendaService.findAllVendaPeriodo(dataInicial, dataFinal);
        List<VendaDto> listDto = list.stream().map(VendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Contabiliza os melhores vendedores
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/melhores_vendedores")
    public ResponseEntity<List<Map<String, Object>>> findAllVendedoresPeriodo(@RequestParam("dataInicial") LocalDate dataInicial, @RequestParam("dataFinal") LocalDate dataFinal) {

        List<Object[]> resultados = vendaService.findVendedoresByNumeroVendas(dataInicial, dataFinal);

        List<Map<String, Object>> response = resultados.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("vendedor", result[0]);
                    map.put("quantidade", result[1]);
                    map.put("totalVendas", result[2]);
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/melhores_clientes")
    public ResponseEntity<List<Map<String, Object>>> findByVendasClientes(@RequestParam("dataInicial") LocalDate dataInicial, @RequestParam("dataFinal") LocalDate dataFinal) {
        List<Object[]> resultados = vendaService.findByVendasClientes(dataInicial, dataFinal);
        List<Map<String, Object>> response = resultados.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cliente", result[0]);
                    map.put("totalVendas", result[1]);
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping("/situacao_vendas")
    public ResponseEntity<List<VendaDto>> findAllBySituacao() {
        List<Venda> vendas = vendaService.findAllBySituacao();
        List<VendaDto> listDto = vendas.stream().map(VendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaDto> findById(@PathVariable Long id){
        Venda objVenda = vendaService.findById(id);
        return ResponseEntity.ok().body(new VendaDto(objVenda));
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<VendaItensDto>> findAllItens(@PathVariable Long id) {
        List<VendaItens> itens = vendaService.findAllItensByVendaId(id);
        List<VendaItensDto> listDto = itens.stream().map(VendaItensDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @PutMapping(value = "/situacao/{id}")
    public ResponseEntity<VendaDto> atualizarSituacao(@PathVariable Long id, @RequestParam Situacao situacao) {

        Venda venda = vendaService.atualizarSituacao(id, situacao);
        return ResponseEntity.ok(new VendaDto(venda));
    }
}
