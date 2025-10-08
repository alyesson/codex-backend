package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import br.com.codex.v1.domain.compras.SolicitacaoItensCompra;
import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import br.com.codex.v1.domain.dto.SolicitacaoItensCompraDto;
import br.com.codex.v1.domain.repository.SolicitacaoCompraRepository;
import br.com.codex.v1.domain.repository.SolicitacaoItensCompraRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoCompraService {

    @Autowired
    private SolicitacaoCompraRepository solicitacaoCompraRepository;

    @Autowired
    private SolicitacaoItensCompraRepository solicitacaoItensCompraRepository;

    @Autowired
    private EnviaEmailService enviaEmailService;

    public SolicitacaoCompra create(SolicitacaoCompraDto solicitacaoCompradto) {

        LocalDate data = LocalDate.now();

        solicitacaoCompradto.setId(null);
        solicitacaoCompradto.setDataSolicitacao(data);
        SolicitacaoCompra objSolicitacaoCompra = new SolicitacaoCompra(solicitacaoCompradto);
        objSolicitacaoCompra = solicitacaoCompraRepository.save(objSolicitacaoCompra);

        // Salvando os itens da solicitação de compra
        for (SolicitacaoItensCompraDto itemDto : solicitacaoCompradto.getItens()) {
            SolicitacaoItensCompra item = new SolicitacaoItensCompra();
            item.setCodigoProduto(itemDto.getCodigoProduto());
            item.setDescricaoProduto(itemDto.getDescricaoProduto());
            item.setQuantidade(itemDto.getQuantidade());
            item.setUnidadeComercial(itemDto.getUnidadeComercial());
            item.setPrecoUnitario(itemDto.getPrecoUnitario());
            item.setSolicitacaoCompra(objSolicitacaoCompra);
            solicitacaoItensCompraRepository.save(item);
        }

        // Enviar e-mail e capturar o retorno
        String resultadoEnvioEmail = enviaEmailService.sendSimpleMail(solicitacaoCompradto);

        // Retornar o resultado do envio de e-mail
        System.out.println(resultadoEnvioEmail); // Para log ou monitoramento

        return objSolicitacaoCompra;
    }

    public List<SolicitacaoItensCompra> findAllItensBySolicitacaoId(Long solicitacaoId) {
        return solicitacaoItensCompraRepository.findBySolicitacaoCompraId(solicitacaoId);
    }

    public List<SolicitacaoCompra> findAllByYearUsuario(String centroCustoUsuario, String solicitante) {

        int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));

        return solicitacaoCompraRepository.findAllByYearUsuario(anoAtual, centroCustoUsuario, solicitante);
    }

    public List<SolicitacaoCompra> findAllByYear() {

        int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));

        return solicitacaoCompraRepository.findAllByYear(anoAtual);
    }

    public void update(Long id, String situacao) {
        solicitacaoCompraRepository.saveSituacao(id, situacao);
    }

    public List<SolicitacaoCompra> findAllBySituacao() {

        int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));

        return solicitacaoCompraRepository.findAllBysituacao(anoAtual);
    }

    public SolicitacaoCompra findById(Long id){
        Optional<SolicitacaoCompra> objSolicitacaoCompra = solicitacaoCompraRepository.findById(id);
        return objSolicitacaoCompra.orElseThrow(() -> new ObjectNotFoundException("solicitacao de compra não encontrada"));
    }

    public List<SolicitacaoCompra> findAllSolicitacaoPeriodo(Date dataInicial, Date dataFinal) {
        return solicitacaoCompraRepository.findAllSolicitacaoPeriodo(dataInicial, dataFinal);
    }
}
