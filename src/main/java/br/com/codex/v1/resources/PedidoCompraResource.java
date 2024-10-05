package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.PedidoCompra;
import br.com.codex.v1.domain.compras.PedidoItensCompra;
import br.com.codex.v1.domain.dto.PedidoCompraDto;
import br.com.codex.v1.domain.dto.PedidoItensCompraDto;
import br.com.codex.v1.service.PedidoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
    @PutMapping(value = "/{id}")
    public ResponseEntity<PedidoCompraDto> update(@PathVariable Integer id, @RequestParam String situacao){
        pedidoCompraService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<PedidoItensCompraDto>> findAllItens(@PathVariable Integer id) {
        List<PedidoItensCompra> itens = pedidoCompraService.findAllItensByPedidoId(id);
        List<PedidoItensCompraDto> listDto = itens.stream().map(PedidoItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping
    public ResponseEntity <List<PedidoCompraDto>> findAll(){
        List<PedidoCompra> objVenda = pedidoCompraService.findAll();
        List<PedidoCompraDto> listDto = objVenda.stream().map(PedidoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PedidoCompraDto> findById(@PathVariable Integer id){
        PedidoCompra obj = pedidoCompraService.findById(id);
        return ResponseEntity.ok().body(new PedidoCompraDto(obj));
    }
}
