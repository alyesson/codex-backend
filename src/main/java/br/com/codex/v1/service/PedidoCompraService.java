package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.PedidoCompra;
import br.com.codex.v1.domain.compras.PedidoItensCompra;
import br.com.codex.v1.domain.dto.PedidoCompraDto;
import br.com.codex.v1.domain.dto.PedidoItensCompraDto;
import br.com.codex.v1.domain.repository.PedidoCompraRepository;
import br.com.codex.v1.domain.repository.PedidoItensCompraRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoCompraService {

    @Autowired
    PedidoCompraRepository pedidoCompraRepository;

    @Autowired
    PedidoItensCompraRepository pedidoItensCompraRepository;

    public PedidoCompra create(PedidoCompraDto pedidoCompradto) {
        pedidoCompradto.setId(null);
        PedidoCompra objPedidoCompra = new PedidoCompra(pedidoCompradto);
        objPedidoCompra = pedidoCompraRepository.save(objPedidoCompra);

        // Salvando os itens da cotação de compra
        for (PedidoItensCompraDto itemDto : pedidoCompradto.getItens()) {
            PedidoItensCompra itensPedido = new PedidoItensCompra();
            itensPedido.setCodigoProduto(itemDto.getCodigoProduto());
            itensPedido.setDescricaoProduto(itemDto.getDescricaoProduto());
            itensPedido.setQuantidade(itemDto.getQuantidade());
            itensPedido.setUnidadeComercial(itemDto.getUnidadeComercial());
            itensPedido.setPrecoUnitario(itemDto.getPrecoUnitario());
            itensPedido.setIpi(itemDto.getIpi());
            itensPedido.setPis(itemDto.getPis());
            itensPedido.setIcms(itemDto.getIcms());
            itensPedido.setCofins(itemDto.getCofins());
            itensPedido.setDesconto(itemDto.getDesconto());
            itensPedido.setPrecoTotal(itemDto.getPrecoTotal());
            itensPedido.setPedidoCompra(objPedidoCompra);
            pedidoItensCompraRepository.save(itensPedido);
        }
        return objPedidoCompra;
    }

    public List<PedidoItensCompra> findAllItensByPedidoId(Long pedidoId) {
        return pedidoItensCompraRepository.findByPedidoCompraId(pedidoId);
    }

    public List<PedidoCompra> findAll() {
        return pedidoCompraRepository.findAll();
    }

    public void update(Long id, String situacao) {
        pedidoCompraRepository.saveSituacao(id, situacao);
    }

    public PedidoCompra findById(Long id) {
        Optional<PedidoCompra> objPedidoCompra = pedidoCompraRepository.findById(id);
        return objPedidoCompra.orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado"));
    }
}
