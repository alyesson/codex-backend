package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.OrcamentoDto;
import br.com.codex.v1.domain.dto.OrcamentoItensDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.OrcamentoItensRepository;
import br.com.codex.v1.domain.repository.OrcamentoRepository;
import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.OrcamentoItens;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

    int anoAtual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private OrcamentoItensRepository orcamentoItensRepository;

    public Orcamento create(OrcamentoDto orcamentoDto){
        orcamentoDto.setId(null);
        Orcamento objOrcamento = new Orcamento(orcamentoDto);
        objOrcamento = orcamentoRepository.save(objOrcamento);

        for(OrcamentoItensDto itemDto : orcamentoDto.getItens()){
            OrcamentoItens item = new OrcamentoItens();
            item.setCodigo(itemDto.getCodigo());
            item.setDescricao(itemDto.getDescricao());
            item.setQuantidade(itemDto.getQuantidade());
            item.setValorUnitario(itemDto.getValorUnitario());
            item.setValorTotal(itemDto.getValorTotal());
            item.setOrcamento(objOrcamento);
            orcamentoItensRepository.save(item);
        }
        return objOrcamento;
    }

    public List<OrcamentoItens> findAllItensByOrcamentoId(Long orcamentoId) {
        return orcamentoItensRepository.findByOrcamentoId(orcamentoId);
    }

    public List<Orcamento> findAllByYearUsuario(Integer ano, String vendedor) {
        return orcamentoRepository.findAllByYearVendedor(ano, vendedor);
    }

    public List<Orcamento> findAllByYear(Integer ano) {
        return orcamentoRepository.findAllByYear(ano);
    }

    public void update(Long id, String situacao) {
        orcamentoRepository.saveSituacao(id, situacao);
    }

    public List<Orcamento> findAllBySituacao() {
        return orcamentoRepository.findAllBySituacao(anoAtual);
    }

    public Orcamento findById(Long id){
        Optional<Orcamento> objOrcamento = orcamentoRepository.findById(id);
        return objOrcamento.orElseThrow(() -> new ObjectNotFoundException("Orçamento não encontrado"));
    }

    public List<Orcamento> findAllOrcamentoPeriodo(Date dataInicial, Date dataFinal) {
        return orcamentoRepository.findAllOrcamentoPeriodo(dataInicial, dataFinal);
    }

    @Transactional
    public Orcamento atualizarSituacao(Long id, Situacao situacao) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Orçamento não encontrado"));

        orcamento.setSituacao(situacao);
        return orcamentoRepository.save(orcamento);
    }
}
