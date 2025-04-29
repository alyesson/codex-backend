package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.Contas;
import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.dto.LancamentoContabilDto;
import br.com.codex.v1.domain.estoque.NotasFiscais;
import br.com.codex.v1.domain.repository.ContasRepository;
import br.com.codex.v1.domain.repository.HistoricoPadraoRepository;
import br.com.codex.v1.domain.repository.LancamentoContabilRepository;
import br.com.codex.v1.domain.repository.NotaFiscalRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LancamentoContabilService {

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;

    @Autowired
    private ContasRepository contasRepository;

    @Autowired
    private HistoricoPadraoRepository historicoPadraoRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    public List<LancamentoContabil> findAllByYearAndMonth(Integer ano, Integer mes) {
        return lancamentoContabilRepository.findAllByYearAndMonth(ano, mes);
    }

    public List<LancamentoContabil> findAllByYearRange(Date anoInicio, Date anoFim) {
        return lancamentoContabilRepository.findAllByYearRange(anoInicio, anoFim);
    }

    public LancamentoContabil findById(Integer id) {
        Optional<LancamentoContabil> objLancamento = lancamentoContabilRepository.findById(id);
        return objLancamento.orElseThrow(() -> new ObjectNotFoundException("Lançamento não encontrado"));
    }

    public void delete(Integer id) {
        lancamentoContabilRepository.deleteById(id);
    }

    public LancamentoContabil create(@Valid LancamentoContabilDto lancamentoContabilDto) {
        lancamentoContabilDto.setId(null);
        LancamentoContabil lancamento = new LancamentoContabil();

        lancamento.setDataLancamento(lancamentoContabilDto.getDataLancamento());
        lancamento.setValor(lancamentoContabilDto.getValor());

        // Busca entidades relacionadas pelo ID
        Contas contaDebito = contasRepository.findById(lancamentoContabilDto.getContaDebitoId())
                .orElseThrow(() -> new ObjectNotFoundException("Conta débito não encontrada"));
        Contas contaCredito = contasRepository.findById(lancamentoContabilDto.getContaCreditoId())
                .orElseThrow(() -> new ObjectNotFoundException("Conta crédito não encontrada"));
        HistoricoPadrao historico = historicoPadraoRepository.findById(lancamentoContabilDto.getHistoricoPadraoId())
                .orElseThrow(() -> new ObjectNotFoundException("Histórico padrão não encontrado"));

        NotasFiscais nota = null;
        if (lancamentoContabilDto.getNotaFiscalOrigemId() != null) {
            nota = notaFiscalRepository.findByNumero(lancamentoContabilDto.getNotaFiscalOrigemId().toString())
                    .orElseThrow(() -> new ObjectNotFoundException("Nota fiscal não encontrada"));
        }

        lancamento.setContaDebito(contaDebito);
        lancamento.setContaCredito(contaCredito);
        lancamento.setHistoricoPadrao(historico);
        lancamento.setNotaFiscalOrigem(nota);
        lancamento.setComplementoHistorico(lancamentoContabilDto.getComplementoHistorico());

        return lancamentoContabilRepository.save(lancamento);
    }
}
