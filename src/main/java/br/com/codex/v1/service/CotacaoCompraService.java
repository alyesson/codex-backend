package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import br.com.codex.v1.domain.dto.CotacaoCompraDto;
import br.com.codex.v1.domain.dto.CotacaoItensCompraDto;
import br.com.codex.v1.domain.repository.CotacaoCompraRepository;
import br.com.codex.v1.domain.repository.CotacaoItensCompraRepository;
import br.com.codex.v1.domain.repository.SolicitacaoCompraRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CotacaoCompraService {

    int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));
    LocalDate data = LocalDate.now();

    @Autowired
    private CotacaoCompraRepository cotacaoCompraRepository;

    @Autowired
    private CotacaoItensCompraRepository cotacaoItensCompraRepository;

    @Autowired
    private SolicitacaoCompraRepository solicitacaoCompraRepository;

    public CotacaoCompra create(CotacaoCompraDto cotacaoCompradto) {

        findByIdSolicitacao(Long.valueOf(cotacaoCompradto.getNumeroSolicitacao()));

        cotacaoCompradto.setId(null);
        cotacaoCompradto.setDataAbertura(data);
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
            item.setFrete(itemDto.getFrete());
            item.setDesconto(itemDto.getDesconto());
            item.setPrecoTotal(itemDto.getPrecoTotal());
            item.setQuantidadePorUnidade(itemDto.getQuantidadePorUnidade());
            item.setQuantidadeTotal(itemDto.getQuantidadeTotal());
            item.setCotacaoCompra(objCotacaoCompra);
            cotacaoItensCompraRepository.save(item);
        }
        return objCotacaoCompra;
    }

    public List<CotacaoItensCompra> findAllItensByCotacaoId(Long solicitacaoId) {
        return cotacaoItensCompraRepository.findByCotacaoCompraId(solicitacaoId);
    }

    public List<CotacaoCompra> findAllByYear() {
        return cotacaoCompraRepository.findAllByYear(anoAtual);
    }

    public void update(Long id, String situacao, String justificativa) {
        CotacaoCompra cotacao = findById(id);
        cotacao.setSituacao(situacao);

        if (justificativa != null && !justificativa.trim().isEmpty()) {
            cotacao.setJustificativa(justificativa);
        }

        cotacaoCompraRepository.save(cotacao);
    }

    public CotacaoCompra findById(Long id) {
        Optional<CotacaoCompra> objCotacaoCompra = cotacaoCompraRepository.findById(id);
        return objCotacaoCompra.orElseThrow(() -> new ObjectNotFoundException("Cotação não encontrada"));
    }

    public List<CotacaoCompra> findAllCotacoesPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return cotacaoCompraRepository.findAllCotacoesPeriodo(dataInicial, dataFinal);
    }

    private void findByIdSolicitacao(Long id){
        Optional<SolicitacaoCompra> objSolicitacao = solicitacaoCompraRepository.findById(id);
        objSolicitacao.orElseThrow(() -> new ObjectNotFoundException("Não existe solicitacao de compra para o número " + id));
    }
}
