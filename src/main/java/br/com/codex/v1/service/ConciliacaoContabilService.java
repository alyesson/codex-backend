package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.dto.ConciliacaoContabilDto;
import br.com.codex.v1.domain.contabilidade.ImportarXml;
import br.com.codex.v1.domain.repository.LancamentoContabilRepository;
import br.com.codex.v1.domain.repository.ImportarXmlRepository;
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
    private ImportarXmlRepository importarXmlRepository;

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;

    //Lista as concilicações do ano e do mes corrente
    public List<ConciliacaoContabilDto> listarConciliacoesMesCorrente() {
        LocalDate hoje = LocalDate.now();
        LocalDate primeiroDia = hoje.withDayOfMonth(1);
        LocalDate ultimoDia = hoje.withDayOfMonth(hoje.lengthOfMonth());

        List<ImportarXml> notas = importarXmlRepository.findByEmissaoBetween(primeiroDia, ultimoDia);
        List<ConciliacaoContabilDto> resultado = new ArrayList<>();

        for (ImportarXml nota : notas) {
            List<LancamentoContabil> lancamentos = lancamentoContabilRepository.findByNotaFiscalOrigem(nota);
            //System.out.println("Nota " + nota.getNumero() + " tem " + lancamentos.size() + " lançamentos");
            BigDecimal totalLancado = lancamentos.stream()
                    .map(LancamentoContabil::getValor)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            boolean conciliado = nota.getValorTotal().compareTo(totalLancado) == 0;
            String obs = lancamentos.isEmpty() ? "Sem lançamentos" : conciliado ? "----" : "Diferença de valor";
            BigDecimal diferenca = nota.getValorTotal().subtract(totalLancado);

            ConciliacaoContabilDto dto = new ConciliacaoContabilDto();
            dto.setNotaFiscalId(nota.getId());
            dto.setNumeroNota(nota.getNumero());
            dto.setValorNota(nota.getValorTotal());
            dto.setValorLancado(totalLancado);
            dto.setConciliado(conciliado);
            dto.setObservacao(obs);
            dto.setDiferenca(diferenca); // <-- Aqui calcula e seta a diferença

            resultado.add(dto);
        }

        return resultado;
    }


    public List<ConciliacaoContabilDto> listarConciliacoesPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<ImportarXml> notas = importarXmlRepository.findByEmissaoBetween(inicio, fim);
        List<ConciliacaoContabilDto> resultado = new ArrayList<>();

        for (ImportarXml nota : notas) {
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
