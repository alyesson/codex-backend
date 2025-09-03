package br.com.codex.v1.service;


import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.PedidoCompra;
import br.com.codex.v1.domain.compras.PedidoItensCompra;
import br.com.codex.v1.domain.dto.PedidoCompraDto;
import br.com.codex.v1.domain.dto.PedidoItensCompraDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CotacaoCompraRepository;
import br.com.codex.v1.domain.repository.PedidoCompraRepository;
import br.com.codex.v1.domain.repository.PedidoItensCompraRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoCompraService {

    int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));
    LocalDate data = LocalDate.now();

    @Autowired
    private PedidoCompraRepository pedidoCompraRepository;

    @Autowired
    private PedidoItensCompraRepository pedidoItensCompraRepository;

    @Autowired
    private CotacaoCompraRepository cotacaoCompraRepository;

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
            itensPedido.setQuantidadePorUnidade(itemDto.getQuantidadePorUnidade());
            itensPedido.setQuantidadeTotal(itemDto.getQuantidadeTotal());
            itensPedido.setPrecoUnitario(itemDto.getPrecoUnitario());
            itensPedido.setIpi(itemDto.getIpi());
            itensPedido.setPis(itemDto.getPis());
            itensPedido.setIcms(itemDto.getIcms());
            itensPedido.setCofins(itemDto.getCofins());
            itensPedido.setDesconto(itemDto.getDesconto());
            itensPedido.setFrete(itemDto.getFrete());
            itensPedido.setPrecoTotal(itemDto.getPrecoTotal());
            itensPedido.setPedidoCompra(objPedidoCompra);
            pedidoItensCompraRepository.save(itensPedido);
        }
        return objPedidoCompra;
    }

    public List<PedidoItensCompra> findAllItensByPedidoId(Long pedidoId) {
        return pedidoItensCompraRepository.findByPedidoCompraId(pedidoId);
    }

    public List<PedidoCompra> findAllByYear() {
        return pedidoCompraRepository.findAllByYear(anoAtual);
    }

    public void aprovaPedido(Long id, String situacao, String aprovador,String justificativa) {
        PedidoCompra pedidoCompra = findById(id);
        pedidoCompra.setSituacao(Situacao.valueOf(situacao));
        pedidoCompra.setDataAprovacao(data);
        pedidoCompra.setAprovador(aprovador);

        if (justificativa != null && !justificativa.trim().isEmpty()) {
            pedidoCompra.setJustificativa(justificativa);
        }

        pedidoCompraRepository.save(pedidoCompra);
    }

    public void rejeitaPedido(Long id, String situacao, String justificativa) {
        PedidoCompra pedidoCompra = findById(id);
        pedidoCompra.setSituacao(Situacao.valueOf(situacao));

        if (justificativa != null && !justificativa.trim().isEmpty()) {
            pedidoCompra.setJustificativa(justificativa);
        }

        pedidoCompraRepository.save(pedidoCompra);
    }

    public PedidoCompra findById(Long id) {
        Optional<PedidoCompra> objPedidoCompra = pedidoCompraRepository.findById(id);
        return objPedidoCompra.orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado"));
    }

    public List<PedidoCompra> findAllCotacoesPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return pedidoCompraRepository.findAllPedidosPeriodo(dataInicial, dataFinal);
    }

    private void findByIdCotacao(Long id){
        Optional<CotacaoCompra> objSolicitacao = cotacaoCompraRepository.findById(id);
        objSolicitacao.orElseThrow(() -> new ObjectNotFoundException("Não existe cotação de compra para o número " + id));
    }

    public void finalizaPedido(Long id, Situacao situacao, LocalDate dataEntregaReal, String justificativa) {
        PedidoCompra pedidoCompra = findById(id);
        pedidoCompra.setSituacao(situacao);
        pedidoCompra.setDataEntregaReal(dataEntregaReal);

        if (justificativa != null && !justificativa.trim().isEmpty()) {
            pedidoCompra.setJustificativa(justificativa);
        }

        pedidoCompraRepository.save(pedidoCompra);
    }
}
