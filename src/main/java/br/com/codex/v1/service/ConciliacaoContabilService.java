package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.dto.ConciliacaoContabilDto;
import br.com.codex.v1.domain.estoque.NotasFiscais;
import br.com.codex.v1.domain.repository.LancamentoContabilRepository;
import br.com.codex.v1.domain.repository.NotaFiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ConciliacaoContabilService {

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;

    //Lista as concilicações do ano e do mes corrente
    public List<ConciliacaoContabilDto> listarConciliacoesMesCorrente() {
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDia = hoje.withDayOfMonth(1);
        LocalDate ultimoDia = hoje.withDayOfMonth(hoje.lengthOfMonth());

        List<NotasFiscais> notas = notaFiscalRepository.findByEmissaoBetween(primeiroDia, ultimoDia);
        List<ConciliacaoContabilDto> resultado = new ArrayList<>();

        for (NotasFiscais nota : notas) {
            List<LancamentoContabil> lancamentos = lancamentoContabilRepository.findByNotaFiscalOrigem(nota);
            BigDecimal totalLancado = lancamentos.stream()
                    .map(LancamentoContabil::getValor)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            boolean conciliado = nota.getValorTotal().compareTo(totalLancado) == 0;
            String obs = lancamentos.isEmpty() ? "Sem lançamentos" : conciliado ? "OK" : "Diferença de valor";

            ConciliacaoContabilDto dto = new ConciliacaoContabilDto();
            dto.setNotaFiscalId(nota.getId());
            dto.setNumeroNota(nota.getNumero());
            dto.setValorNota(nota.getValorTotal());
            dto.setValorLancado(totalLancado);
            dto.setConciliado(conciliado);
            dto.setObservacao(obs);

            resultado.add(dto);
        }
        return resultado;
    }

    public List<ConciliacaoContabilDto> listarConciliacoesPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<NotasFiscais> notas = notaFiscalRepository.findByEmissaoBetween(inicio, fim);
        List<ConciliacaoContabilDto> resultado = new ArrayList<>();

        for (NotasFiscais nota : notas) {
            List<LancamentoContabil> lancamentos = lancamentoContabilRepository.findByNotaFiscalOrigem(nota);
            BigDecimal totalLancado = lancamentos.stream()
                    .map(LancamentoContabil::getValor)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            boolean conciliado = nota.getValorTotal().compareTo(totalLancado) == 0;
            String obs = lancamentos.isEmpty() ? "Sem lançamentos" : conciliado ? "OK" : "Diferença de valor";

            ConciliacaoContabilDto dto = new ConciliacaoContabilDto();
            dto.setNotaFiscalId(nota.getId());
            dto.setNumeroNota(nota.getNumero());
            dto.setValorNota(nota.getValorTotal());
            dto.setValorLancado(totalLancado);
            dto.setConciliado(conciliado);
            dto.setObservacao(obs);

            resultado.add(dto);
        }
        return resultado;
    }
}
