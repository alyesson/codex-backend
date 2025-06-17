package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import br.com.codex.v1.domain.dto.CotacaoCompraDto;
import br.com.codex.v1.domain.dto.CotacaoItensCompraDto;
import br.com.codex.v1.domain.repository.CotacaoCompraRepository;
import br.com.codex.v1.domain.repository.CotacaoItensCompraRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CotacaoCompraService {

    @Autowired
    CotacaoCompraRepository cotacaoCompraRepository;

    @Autowired
    CotacaoItensCompraRepository cotacaoItensCompraRepository;

    public CotacaoCompra create(CotacaoCompraDto cotacaoCompradto) {
        cotacaoCompradto.setId(null);
        CotacaoCompra objCotacaoCompra = new CotacaoCompra(cotacaoCompradto);
        objCotacaoCompra = cotacaoCompraRepository.save(objCotacaoCompra);

        // Salvando os itens da cotação de compra
        for (CotacaoItensCompraDto itemDto : cotacaoCompradto.getItens()) {
            CotacaoItensCompra item = new CotacaoItensCompra();
            item.setCodigoProduto(itemDto.getCodigoProduto());
            item.setDescricaoProduto(itemDto.getDescricaoProduto());
            item.setQuantidade(itemDto.getQuantidade());
            item.setUnidadeComercial(itemDto.getUnidadeComercial());
            item.setPrecoUnitario(itemDto.getPrecoUnitario());
            item.setIpi(itemDto.getIpi());
            item.setPis(itemDto.getPis());
            item.setIcms(itemDto.getIcms());
            item.setCofins(itemDto.getCofins());
            item.setDesconto(itemDto.getDesconto());
            item.setPrecoTotal(itemDto.getPrecoTotal());
            item.setCotacaoCompra(objCotacaoCompra);
            cotacaoItensCompraRepository.save(item);
        }
        return objCotacaoCompra;
    }

    public List<CotacaoItensCompra> findAllItensByCotacaoId(Long solicitacaoId) {
        return cotacaoItensCompraRepository.findByCotacaoCompraId(solicitacaoId);
    }

    public List<CotacaoCompra> findAll() {
        return cotacaoCompraRepository.findAll();
    }

    public void update(Long id, String situacao) {
        cotacaoCompraRepository.saveSituacao(id, situacao);
    }

    public CotacaoCompra findById(Long id) {
        Optional<CotacaoCompra> objCotacaoCompra = cotacaoCompraRepository.findById(id);
        return objCotacaoCompra.orElseThrow(() -> new ObjectNotFoundException("Cotação não encontrada"));
    }

    public List<CotacaoCompra> findAllCotacoesPeriodo(Date dataInicial, Date dataFinal) {
        return cotacaoCompraRepository.findAllCotacoesPeriodo(dataInicial, dataFinal);
    }
}
