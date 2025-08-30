package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.OrdemCompra;
import br.com.codex.v1.domain.compras.OrdemItensCompra;
import br.com.codex.v1.domain.dto.OrdemCompraDto;
import br.com.codex.v1.domain.dto.OrdemItensCompraDto;
import br.com.codex.v1.domain.repository.OrdemCompraRepository;
import br.com.codex.v1.domain.repository.OrdemItensCompraRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdemCompraService {

    int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));

    @Autowired
    private OrdemCompraRepository ordemCompraRepository;

    @Autowired
    private OrdemItensCompraRepository ordemItensCompraRepository;

    @Autowired
    private EnviaEmailService enviaEmailService;

    public OrdemCompra create(OrdemCompraDto ordemCompradto) {
        ordemCompradto.setId(null);
        OrdemCompra objOrdemCompra = new OrdemCompra(ordemCompradto);
        objOrdemCompra = ordemCompraRepository.save(objOrdemCompra);

        // Salvando os itens da solicitação de compra
        for (OrdemItensCompraDto itemDto : ordemCompradto.getItens()) {
            OrdemItensCompra item = new OrdemItensCompra();
            item.setCodigoProduto(itemDto.getCodigoProduto());
            item.setDescricaoProduto(itemDto.getDescricaoProduto());
            item.setQuantidade(itemDto.getQuantidade());
            item.setUnidadeComercial(itemDto.getUnidadeComercial());
            item.setPrecoUnitario(itemDto.getPrecoUnitario());
            item.setOrdemCompra(objOrdemCompra);
            ordemItensCompraRepository.save(item);
        }

        // Enviar e-mail e capturar o retorno
        String resultadoEnvioEmail = enviaEmailService.sendSimpleMail(ordemCompradto);

        // Retornar o resultado do envio de e-mail
        System.out.println(resultadoEnvioEmail); // Para log ou monitoramento

        return objOrdemCompra;
    }

    public List<OrdemItensCompra> findAllItensByOrdemId(Long solicitacaoId) {
        return ordemItensCompraRepository.findByOrdemCompraId(solicitacaoId);
    }

    public List<OrdemCompra> findAllByYearUsuario(Integer ano, String centroCustoUsuario, String solicitante) {
        return ordemCompraRepository.findAllByYearUsuario(ano, centroCustoUsuario, solicitante);
    }

    public List<OrdemCompra> findAllByYear(Integer ano, String centroCustoUsuario) {
        return ordemCompraRepository.findAllByYear(ano, centroCustoUsuario);
    }

    public void update(Long id, String situacao) {
        ordemCompraRepository.saveSituacao(id, situacao);
    }

    public List<OrdemCompra> findAllBySituacao() {
        return ordemCompraRepository.findAllBysituacao(anoAtual);
    }

    public OrdemCompra findById(Long id){
        Optional<OrdemCompra> objSolicitacaoCompra = ordemCompraRepository.findById(id);
        return objSolicitacaoCompra.orElseThrow(() -> new ObjectNotFoundException("ordem de compra não encontrada"));
    }

    public List<OrdemCompra> findAllOrdemPeriodo(Date dataInicial, Date dataFinal) {
        return ordemCompraRepository.findAllOrdemPeriodo(dataInicial, dataFinal);
    }
}
