package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.PedidoCompra;
import br.com.codex.v1.domain.compras.PedidoItensCompra;
import br.com.codex.v1.domain.dto.PedidoCompraDto;
import br.com.codex.v1.domain.dto.PedidoItensCompraDto;
import br.com.codex.v1.service.PedidoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/pedido_compra")
public class PedidoCompraResource {

    @Autowired
    private PedidoCompraService pedidoCompraService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PostMapping
    public ResponseEntity<PedidoCompraDto> create(@Valid @RequestBody PedidoCompraDto pedidoCompraDto) {
        PedidoCompra obj = pedidoCompraService.create(pedidoCompraDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/pedidos_ano")
    public ResponseEntity <List<PedidoCompraDto>> findAllByYear(){
        List<PedidoCompra> objPedido = pedidoCompraService.findAllByYear();
        List<PedidoCompraDto> listDto = objPedido.stream().map(PedidoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/pedidos_periodo")
    public ResponseEntity<List<PedidoCompraDto>> findAllPedidoPeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                        @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<PedidoCompra> list = pedidoCompraService.findAllCotacoesPeriodo(dataInicial, dataFinal);
        List<PedidoCompraDto> listDto = list.stream().map(PedidoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PedidoCompraDto> atualizarSituacao(@PathVariable Long id, @RequestParam String situacao, @RequestParam(required = false) String justificativa) {
        pedidoCompraService.update(id, situacao, justificativa);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<PedidoItensCompraDto>> findAllItens(@PathVariable Long id) {
        List<PedidoItensCompra> itens = pedidoCompraService.findAllItensByPedidoId(id);
        List<PedidoItensCompraDto> listDto = itens.stream().map(PedidoItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity <PedidoCompraDto> findById(@PathVariable Long id){
        PedidoCompra objPedido = pedidoCompraService.findById(id);
        return ResponseEntity.ok().body(new PedidoCompraDto(objPedido));
    }
}
