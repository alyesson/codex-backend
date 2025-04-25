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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoCompraService {

    int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));

    @Autowired
    private SolicitacaoCompraRepository solicitacaoCompraRepository;

    @Autowired
    private SolicitacaoItensCompraRepository solicitacaoItensCompraRepository;

    @Autowired
    private EnviaEmailService enviaEmailService;

    public SolicitacaoCompra create(SolicitacaoCompraDto solicitacaoCompradto) {
        solicitacaoCompradto.setId(null);
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

    public List<SolicitacaoItensCompra> findAllItensBySolicitacaoId(Integer solicitacaoId) {
        return solicitacaoItensCompraRepository.findBySolicitacaoCompraId(solicitacaoId);
    }

    public List<SolicitacaoCompra> findAllByYearUsuario(Integer ano, String centroCustoUsuario, String solicitante) {
        return solicitacaoCompraRepository.findAllByYearUsuario(ano, centroCustoUsuario, solicitante);
    }

    public List<SolicitacaoCompra> findAllByYear(Integer ano, String centroCustoUsuario) {
        return solicitacaoCompraRepository.findAllByYear(ano, centroCustoUsuario);
    }

    public void update(Integer id, String situacao) {
        solicitacaoCompraRepository.saveSituacao(id, situacao);
    }

    public List<SolicitacaoCompra> findAllBySituacao() {
        return solicitacaoCompraRepository.findAllBysituacao(anoAtual);
    }

    public SolicitacaoCompra findById(Integer id){
        Optional<SolicitacaoCompra> objSolicitacaoCompra = solicitacaoCompraRepository.findById(id);
        return objSolicitacaoCompra.orElseThrow(() -> new ObjectNotFoundException("Solicitação não encontrada"));
    }

    public List<SolicitacaoCompra> findAllSolicitacoesPeriodo(Date dataInicial, Date dataFinal) {
        return solicitacaoCompraRepository.findAllSolicitacoesPeriodo(dataInicial, dataFinal);
    }
}
