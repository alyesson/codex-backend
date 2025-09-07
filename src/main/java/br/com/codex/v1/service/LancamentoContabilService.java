package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.*;
import br.com.codex.v1.domain.dto.BalancoPatrimonialDto;
import br.com.codex.v1.domain.dto.GrupoContabilDto;
import br.com.codex.v1.domain.dto.ItemContabilDto;
import br.com.codex.v1.domain.fiscal.ImportarXml;
import br.com.codex.v1.domain.dto.LancamentoContabilDto;
import br.com.codex.v1.domain.repository.ContasRepository;
import br.com.codex.v1.domain.repository.HistoricoPadraoRepository;
import br.com.codex.v1.domain.repository.LancamentoContabilRepository;
import br.com.codex.v1.domain.repository.ImportarXmlRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LancamentoContabilService {

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;

    @Autowired
    private ContasRepository contasRepository;

    @Autowired
    private HistoricoPadraoRepository historicoPadraoRepository;

    @Autowired
    private ImportarXmlRepository importarXmlRepository;

    public List<LancamentoContabil> findAllByYearAndMonth(Integer ano, Integer mes) {
        return lancamentoContabilRepository.findAllByYearAndMonth(ano, mes);
    }

    public List<LancamentoContabil> findAllByYearRange(Date anoInicio, Date anoFim) {
        return lancamentoContabilRepository.findAllByYearRange(anoInicio, anoFim);
    }

    public LancamentoContabil findById(Long id) {
        Optional<LancamentoContabil> objLancamento = lancamentoContabilRepository.findById(id);
        return objLancamento.orElseThrow(() -> new ObjectNotFoundException("Lançamento não encontrado"));
    }

    public void delete(Long id) {
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

        ImportarXml nota = null;
        if (lancamentoContabilDto.getNotaFiscalOrigemId() != null) {
            nota = importarXmlRepository.findById(lancamentoContabilDto.getNotaFiscalOrigemId())
                    .orElseThrow(() -> new ObjectNotFoundException("Nota fiscal não encontrada"));
        }

        lancamento.setContaDebito(contaDebito);
        lancamento.setContaCredito(contaCredito);
        lancamento.setHistoricoPadrao(historico);
        lancamento.setNotaFiscalOrigem(nota);
        lancamento.setComplementoHistorico(lancamentoContabilDto.getComplementoHistorico());

        return lancamentoContabilRepository.save(lancamento);
    }

    public LancamentoContabil update(Long id, LancamentoContabilDto lancamentoContabilDto) {
        LancamentoContabil lancamento = findById(id);
        lancamentoContabilDto.setId(id);
        lancamento.setValor(lancamentoContabilDto.getValor());

        Contas contaDebito = contasRepository.findById(lancamentoContabilDto.getContaDebitoId())
                .orElseThrow(() -> new ObjectNotFoundException("Conta débito não encontrada"));
        Contas contaCredito = contasRepository.findById(lancamentoContabilDto.getContaCreditoId())
                .orElseThrow(() -> new ObjectNotFoundException("Conta crédito não encontrada"));
        HistoricoPadrao historico = historicoPadraoRepository.findById(lancamentoContabilDto.getHistoricoPadraoId())
                .orElseThrow(() -> new ObjectNotFoundException("Histórico padrão não encontrado"));
        ImportarXml nota = importarXmlRepository.findById(lancamentoContabilDto.getNotaFiscalOrigemId())
                    .orElseThrow(() -> new ObjectNotFoundException("Nota fiscal não encontrada"));

        lancamento.setContaDebito(contaDebito);
        lancamento.setContaCredito(contaCredito);
        lancamento.setHistoricoPadrao(historico);
        lancamento.setNotaFiscalOrigem(nota);
        lancamento.setComplementoHistorico(lancamentoContabilDto.getComplementoHistorico());

        return lancamentoContabilRepository.save(lancamento);
    }

    public BalancoPatrimonialDto gerarBalancoPatrimonial(Date dataInicial, Date dataFinal) {
        // 1. Buscar lançamentos do período
        List<LancamentoContabil> lancamentos = findAllByYearRange(dataInicial, dataFinal);

        // 2. Calcular saldos das contas
        Map<Contas, BigDecimal> saldosContas = calcularSaldosContas(lancamentos);

        // 3. Buscar estrutura de contas DO BANCO (não fixa!)
        List<Contas> todasContas = contasRepository.findAll();

        // 4. Classificar dinamicamente
        List<GrupoContabilDto> ativo = classificarContasDinamicamente(saldosContas, todasContas, "1");
        List<GrupoContabilDto> passivo = classificarContasDinamicamente(saldosContas, todasContas, "2");
        List<GrupoContabilDto> patrimonio = classificarContasDinamicamente(saldosContas, todasContas, "3");

        // 5. Retornar resposta estruturada
        return new BalancoPatrimonialDto(ativo, passivo, patrimonio);
    }

    private Map<Contas, BigDecimal> calcularSaldosContas(List<LancamentoContabil> lancamentos) {
        Map<Contas, BigDecimal> saldos = new HashMap<>();

        for (LancamentoContabil lancamento : lancamentos) {
            Contas contaDebito = lancamento.getContaDebito();
            Contas contaCredito = lancamento.getContaCredito();
            BigDecimal valor = lancamento.getValor();

            // Atualiza saldo da conta débito (aumenta)
            if (contaDebito != null) {
                BigDecimal saldoAtualDebito = saldos.getOrDefault(contaDebito, BigDecimal.ZERO);
                saldos.put(contaDebito, saldoAtualDebito.add(valor));
            }

            // Atualiza saldo da conta crédito (diminui)
            if (contaCredito != null) {
                BigDecimal saldoAtualCredito = saldos.getOrDefault(contaCredito, BigDecimal.ZERO);
                saldos.put(contaCredito, saldoAtualCredito.subtract(valor));
            }
        }

        return saldos;
    }

    private List<GrupoContabilDto> classificarContasDinamicamente(Map<Contas, BigDecimal> saldos,List<Contas> todasContas,String classe) {

        List<GrupoContabilDto> grupos = new ArrayList<>();

        // Busca todas as contas desta classe (ex: "1" para Ativo)
        List<Contas> contasDaClasse = todasContas.stream()
                .filter(conta -> conta.getConta().startsWith(classe + "."))
                .toList();

        // Agrupa por subclasse (ex: "1.01", "1.02", etc.)
        Map<String, List<Contas>> contasPorSubclasse = contasDaClasse.stream()
                .collect(Collectors.groupingBy(conta -> {
                    // Pega os dois primeiros níveis (ex: "1.01" de "1.01.001")
                    String[] partes = conta.getConta().split("\\.");
                    return partes.length >= 2 ? partes[0] + "." + partes[1] : conta.getConta();
                }));

        // Cria os grupos
        for (Map.Entry<String, List<Contas>> entry : contasPorSubclasse.entrySet()) {
            String subclasse = entry.getKey();
            List<Contas> contasDoGrupo = entry.getValue();

            List<ItemContabilDto> itens = new ArrayList<>();

            for (Contas conta : contasDoGrupo) {
                BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
                // Se o saldo for zero, pode optar por não mostrar
                if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                    itens.add(new ItemContabilDto(conta.getNome(), saldo));
                }
            }

            if (!itens.isEmpty()) {
                // Busca o nome do grupo (pega o nome da primeira conta do grupo)
                String nomeGrupo = contasDoGrupo.get(0).getNome();
                // Para melhorar: você pode criar uma tabela de grupos com nomes amigáveis

                grupos.add(new GrupoContabilDto(nomeGrupo, itens));
            }
        }

        return grupos;
    }

    // Métudo auxiliar para debug - pode ser removido depois
    private void logContasPorGrupo(Map<String, List<Contas>> contasPorSubclasse) {
        for (Map.Entry<String, List<Contas>> entry : contasPorSubclasse.entrySet()) {
            System.out.println("Grupo: " + entry.getKey());
            for (Contas conta : entry.getValue()) {
                System.out.println("  - " + conta.getConta() + " - " + conta.getNome());
            }
        }
    }
}
