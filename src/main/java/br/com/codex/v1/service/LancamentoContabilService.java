package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.*;
import br.com.codex.v1.domain.dto.*;
import br.com.codex.v1.domain.fiscal.ImportarXml;
import br.com.codex.v1.domain.repository.*;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    private ConfiguracaoContabilRepository configuracaoContabilRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<LancamentoContabil> findAllByYearAndMonth(Integer ano, Integer mes) {
        return lancamentoContabilRepository.findAllByYearAndMonth(ano, mes);
    }

    public List<LancamentoContabil> findAllByYearRange(LocalDate anoInicio, LocalDate anoFim) {
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

    public BalancoPatrimonialDto gerarBalancoPatrimonial(LocalDate dataInicial, LocalDate dataFinal) {
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

    @Cacheable(value = "relatorios", key = "#empresaId + '-' + #dataInicial.toString() + '-' + #dataFinal.toString()")
    public DREDto gerarDRE(LocalDate dataInicial, LocalDate dataFinal, Long empresaId) {

        if (dataInicial.isAfter(dataFinal)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        if (!empresaRepository.existsById(empresaId)) {
            throw new ObjectNotFoundException("Empresa não encontrada");
        }

        List<ConfiguracaoContabil> configs = configuracaoContabilRepository.findByEmpresaId(empresaId);

        if (!configs.isEmpty()) {
            return gerarDREComConfiguracao(dataInicial, dataFinal, configs);
        }

        // 2. Se não houver configuração, usa auto-detecção
        return gerarDREAutoDetectado(dataInicial, dataFinal);
    }

    private List<GrupoContabilDto> classificarPorConfiguracao(Map<Contas, BigDecimal> saldos, List<Contas> todasContas, List<ConfiguracaoContabil> configs, String tipo) {

        List<GrupoContabilDto> grupos = new ArrayList<>();

        // Filtra configurações do tipo especificado
        List<ConfiguracaoContabil> configsFiltradas = configs.stream()
                .filter(c -> tipo.equals(c.getTipo()))
                .collect(Collectors.toList());

        for (ConfiguracaoContabil config : configsFiltradas) {
            List<Contas> contasFiltradas = filtrarContas(todasContas, config);
            List<ItemContabilDto> itens = new ArrayList<>();

            for (Contas conta : contasFiltradas) {
                BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
                if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                    itens.add(new ItemContabilDto(conta.getNome(), saldo.abs()));
                }
            }

            if (!itens.isEmpty()) {
                grupos.add(new GrupoContabilDto(
                        config.getNomeGrupo() != null ? config.getNomeGrupo() : config.getValor(),
                        itens
                ));
            }
        }

        return grupos;
    }

    private List<Contas> filtrarContas(List<Contas> todasContas, ConfiguracaoContabil config) {
        switch (config.getCriterio()) {
            case "PREFIXO_CODIGO":
                return todasContas.stream()
                        .filter(c -> c.getConta().startsWith(config.getValor()))
                        .collect(Collectors.toList());

            case "TIPO":
                return todasContas.stream()
                        .filter(c -> config.getValor().equals(c.getTipo()))
                        .collect(Collectors.toList());

            case "NATUREZA":
                return todasContas.stream()
                        .filter(c -> config.getValor().equals(c.getNatureza()))
                        .collect(Collectors.toList());

            case "UTILIDADE":
                return todasContas.stream()
                        .filter(c -> config.getValor().equals(c.getUtilidade()))
                        .collect(Collectors.toList());

            default:
                return new ArrayList<>();
        }
    }

    private List<GrupoContabilDto> autoDetectarReceitas(Map<Contas, BigDecimal> saldos, List<Contas> todasContas) {
        List<GrupoContabilDto> grupos = new ArrayList<>();

        // Tenta vários critérios diferentes
        List<Contas> contasReceitas = todasContas.stream()
                .filter(c ->
                        // Critério 1: Código começa com padrões comuns de receita
                        c.getConta().startsWith("4.") ||
                                c.getConta().startsWith("3.1") ||
                                // Critério 2: Nome contém palavras-chave de receita
                                c.getNome().toLowerCase().contains("venda") ||
                                c.getNome().toLowerCase().contains("receita") ||
                                c.getNome().toLowerCase().contains("faturamento") ||
                                // Critério 3: Tipo indica receita
                                "Receitas".equals(c.getTipo())
                )
                .collect(Collectors.toList());

        // Agrupa por subcategoria
        Map<String, List<Contas>> agrupadas = contasReceitas.stream()
                .collect(Collectors.groupingBy(this::determinarCategoriaReceita));

        for (Map.Entry<String, List<Contas>> entry : agrupadas.entrySet()) {
            List<ItemContabilDto> itens = new ArrayList<>();
            for (Contas conta : entry.getValue()) {
                BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
                if (saldo.compareTo(BigDecimal.ZERO) > 0) { // Receitas são positivas
                    itens.add(new ItemContabilDto(conta.getNome(), saldo));
                }
            }
            if (!itens.isEmpty()) {
                grupos.add(new GrupoContabilDto(entry.getKey(), itens));
            }
        }

        return grupos;
    }

    private String determinarCategoriaReceita(Contas conta) {
        if (conta.getNome().toLowerCase().contains("venda")) return "Vendas";
        if (conta.getNome().toLowerCase().contains("serviço")) return "Serviços";
        if (conta.getNome().toLowerCase().contains("juros")) return "Receitas Financeiras";
        return "Outras Receitas";
    }

    private DREDto gerarDREComConfiguracao(LocalDate dataInicial, LocalDate dataFinal, List<ConfiguracaoContabil> configs) {
        // 1. Buscar lançamentos do período
        List<LancamentoContabil> lancamentos = findAllByYearRange(dataInicial, dataFinal);

        // 2. Calcular saldos das contas
        Map<Contas, BigDecimal> saldosContas = calcularSaldosContas(lancamentos);

        // 3. Buscar estrutura de contas DO BANCO
        List<Contas> todasContas = contasRepository.findAll();

        // 4. Classificar baseado na configuração
        List<GrupoContabilDto> receitas = classificarPorConfiguracao(saldosContas, todasContas, configs, "RECEITA");
        List<GrupoContabilDto> custos = classificarPorConfiguracao(saldosContas, todasContas, configs, "CUSTO");
        List<GrupoContabilDto> despesas = classificarPorConfiguracao(saldosContas, todasContas, configs, "DESPESA");

        // 5. Retornar DRE estruturado
        return new DREDto(receitas, custos, despesas);
    }

    private DREDto gerarDREAutoDetectado(LocalDate dataInicial, LocalDate dataFinal) {
        // 1. Buscar lançamentos do período
        List<LancamentoContabil> lancamentos = findAllByYearRange(dataInicial, dataFinal);

        // 2. Calcular saldos das contas
        Map<Contas, BigDecimal> saldosContas = calcularSaldosContas(lancamentos);

        // 3. Buscar estrutura de contas DO BANCO
        List<Contas> todasContas = contasRepository.findAll();

        // 4. Auto-detecta basedo em padrões comuns
        List<GrupoContabilDto> receitas = autoDetectarReceitas(saldosContas, todasContas);
        List<GrupoContabilDto> custos = autoDetectarCustos(saldosContas, todasContas);
        List<GrupoContabilDto> despesas = autoDetectarDespesas(saldosContas, todasContas);

        // 5. Retornar DRE estruturado
        return new DREDto(receitas, custos, despesas);
    }

    public DFCDto gerarDFC(LocalDate dataInicial, LocalDate dataFinal, Long empresaId) {
        // 1. Validar parâmetros
        if (dataInicial.isAfter(dataFinal)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        if (!empresaRepository.existsById(empresaId)) {
            throw new ObjectNotFoundException("Empresa não encontrada");
        }

        // 2. Buscar lançamentos do período
        List<LancamentoContabil> lancamentos = findAllByYearRange(dataInicial, dataFinal);

        // 3. Calcular saldos das contas
        Map<Contas, BigDecimal> saldosContas = calcularSaldosContas(lancamentos);

        // 4. Buscar estrutura de contas
        List<Contas> todasContas = contasRepository.findAll();

        // 5. Classificar fluxos
        List<FluxoCaixaItemDto> fluxoOperacional = classificarFluxoOperacional(saldosContas, todasContas);
        List<FluxoCaixaItemDto> fluxoInvestimento = classificarFluxoInvestimento(saldosContas, todasContas);
        List<FluxoCaixaItemDto> fluxoFinanciamento = classificarFluxoFinanciamento(saldosContas, todasContas);

        // 6. Calcular totais
        BigDecimal totalOperacional = calcularTotalFluxo(fluxoOperacional);
        BigDecimal totalInvestimento = calcularTotalFluxo(fluxoInvestimento);
        BigDecimal totalFinanciamento = calcularTotalFluxo(fluxoFinanciamento);

        // 7. Calcular saldos de caixa
        BigDecimal saldoInicial = calcularSaldoInicialCaixa(dataInicial, empresaId);
        BigDecimal variacaoPeriodo = totalOperacional.add(totalInvestimento).add(totalFinanciamento);
        BigDecimal saldoFinal = saldoInicial.add(variacaoPeriodo);

        // 8. Retornar DFC
        return new DFCDto(fluxoOperacional, fluxoInvestimento, fluxoFinanciamento,
                totalOperacional, totalInvestimento, totalFinanciamento,
                saldoInicial, variacaoPeriodo, saldoFinal);
    }

    private List<FluxoCaixaItemDto> classificarFluxoOperacional(Map<Contas, BigDecimal> saldos, List<Contas> todasContas) {
        List<FluxoCaixaItemDto> fluxo = new ArrayList<>();

        // Identificar contas de fluxo operacional (ex: contas que começam com 6, 7, etc.)
        List<Contas> contasOperacionais = todasContas.stream()
                .filter(c -> c.getConta().startsWith("6.") || c.getConta().startsWith("7."))
                .toList();

        for (Contas conta : contasOperacionais) {
            BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
            if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                fluxo.add(new FluxoCaixaItemDto(conta.getNome(), saldo));
            }
        }

        return fluxo;
    }

    private List<FluxoCaixaItemDto> classificarFluxoInvestimento(Map<Contas, BigDecimal> saldos, List<Contas> todasContas) {
        List<FluxoCaixaItemDto> fluxo = new ArrayList<>();

        // Identificar contas de investimento (ex: contas de ativo imobilizado, investimentos)
        List<Contas> contasInvestimento = todasContas.stream()
                .filter(c -> c.getConta().startsWith("2.2") || c.getConta().startsWith("2.3") ||
                        c.getNome().toLowerCase().contains("investimento") ||
                        c.getNome().toLowerCase().contains("imobilizado"))
                .toList();

        for (Contas conta : contasInvestimento) {
            BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
            if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                fluxo.add(new FluxoCaixaItemDto(conta.getNome(), saldo));
            }
        }

        return fluxo;
    }

    private List<FluxoCaixaItemDto> classificarFluxoFinanciamento(Map<Contas, BigDecimal> saldos, List<Contas> todasContas) {
        List<FluxoCaixaItemDto> fluxo = new ArrayList<>();

        // Identificar contas de financiamento (ex: empréstimos, financiamentos, capital)
        List<Contas> contasFinanciamento = todasContas.stream()
                .filter(c -> c.getConta().startsWith("2.1") ||
                        c.getNome().toLowerCase().contains("empréstimo") ||
                        c.getNome().toLowerCase().contains("financiamento") ||
                        c.getNome().toLowerCase().contains("capital"))
                .toList();

        for (Contas conta : contasFinanciamento) {
            BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
            if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                fluxo.add(new FluxoCaixaItemDto(conta.getNome(), saldo));
            }
        }

        return fluxo;
    }

    private BigDecimal calcularTotalFluxo(List<FluxoCaixaItemDto> fluxo) {
        return fluxo.stream()
                .map(FluxoCaixaItemDto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularSaldoInicialCaixa(LocalDate dataInicial, Long empresaId) {
        // Buscar saldo inicial de caixa (contas que começam com 1.01)
        LocalDate dataInicioPeriodo = dataInicial.minusYears(1); // Ajuste conforme necessário

        List<LancamentoContabil> lancamentosPeriodoAnterior = findAllByYearRange(
                dataInicioPeriodo, dataInicial.minusDays(1));

        Map<Contas, BigDecimal> saldosPeriodoAnterior = calcularSaldosContas(lancamentosPeriodoAnterior);

        // Filtrar apenas contas de caixa
        return saldosPeriodoAnterior.entrySet().stream()
                .filter(entry -> entry.getKey().getConta().startsWith("1.01"))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<GrupoContabilDto> autoDetectarCustos(Map<Contas, BigDecimal> saldos, List<Contas> todasContas) {
        List<GrupoContabilDto> grupos = new ArrayList<>();

        // Tenta vários critérios diferentes para custos
        List<Contas> contasCustos = todasContas.stream()
                .filter(c ->
                        // Critério 1: Código começa com padrões comuns de custo
                        c.getConta().startsWith("5.") ||
                                c.getConta().startsWith("3.2") ||
                                // Critério 2: Nome contém palavras-chave de custo
                                c.getNome().toLowerCase().contains("custo") ||
                                c.getNome().toLowerCase().contains("cmv") ||
                                c.getNome().toLowerCase().contains("mercadoria") ||
                                // Critério 3: Tipo indica custo
                                "Custos".equals(c.getTipo())
                )
                .collect(Collectors.toList());

        // Agrupa por subcategoria
        Map<String, List<Contas>> agrupadas = contasCustos.stream()
                .collect(Collectors.groupingBy(this::determinarCategoriaCusto));

        for (Map.Entry<String, List<Contas>> entry : agrupadas.entrySet()) {
            List<ItemContabilDto> itens = new ArrayList<>();
            for (Contas conta : entry.getValue()) {
                BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
                if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                    itens.add(new ItemContabilDto(conta.getNome(), saldo.abs()));
                }
            }
            if (!itens.isEmpty()) {
                grupos.add(new GrupoContabilDto(entry.getKey(), itens));
            }
        }

        return grupos;
    }

    private List<GrupoContabilDto> autoDetectarDespesas(Map<Contas, BigDecimal> saldos, List<Contas> todasContas) {
        List<GrupoContabilDto> grupos = new ArrayList<>();

        // Tenta vários critérios diferentes para despesas
        List<Contas> contasDespesas = todasContas.stream()
                .filter(c ->
                        // Critério 1: Código começa com padrões comuns de despesa
                        c.getConta().startsWith("6.") ||
                                c.getConta().startsWith("2.1") ||
                                // Critério 2: Nome contém palavras-chave de despesa
                                c.getNome().toLowerCase().contains("despesa") ||
                                c.getNome().toLowerCase().contains("salário") ||
                                c.getNome().toLowerCase().contains("aluguel") ||
                                c.getNome().toLowerCase().contains("energia") ||
                                c.getNome().toLowerCase().contains("água") ||
                                c.getNome().toLowerCase().contains("telefone") ||
                                // Critério 3: Tipo indica despesa
                                "Despesas".equals(c.getTipo())
                )
                .collect(Collectors.toList());

        // Agrupa por subcategoria
        Map<String, List<Contas>> agrupadas = contasDespesas.stream()
                .collect(Collectors.groupingBy(this::determinarCategoriaDespesa));

        for (Map.Entry<String, List<Contas>> entry : agrupadas.entrySet()) {
            List<ItemContabilDto> itens = new ArrayList<>();
            for (Contas conta : entry.getValue()) {
                BigDecimal saldo = saldos.getOrDefault(conta, BigDecimal.ZERO);
                if (saldo.compareTo(BigDecimal.ZERO) != 0) {
                    itens.add(new ItemContabilDto(conta.getNome(), saldo.abs()));
                }
            }
            if (!itens.isEmpty()) {
                grupos.add(new GrupoContabilDto(entry.getKey(), itens));
            }
        }

        return grupos;
    }

    private String determinarCategoriaCusto(Contas conta) {
        if (conta.getNome().toLowerCase().contains("mercadoria")) return "Custo de Mercadorias";
        if (conta.getNome().toLowerCase().contains("produção")) return "Custo de Produção";
        if (conta.getNome().toLowerCase().contains("serviço")) return "Custo de Serviços";
        return "Outros Custos";
    }

    private String determinarCategoriaDespesa(Contas conta) {
        if (conta.getNome().toLowerCase().contains("salário") || conta.getNome().toLowerCase().contains("pro labore"))
            return "Despesas com Pessoal";
        if (conta.getNome().toLowerCase().contains("aluguel")) return "Despesas de Aluguel";
        if (conta.getNome().toLowerCase().contains("energia") || conta.getNome().toLowerCase().contains("água") || conta.getNome().toLowerCase().contains("telefone"))
            return "Despesas Operacionais";
        if (conta.getNome().toLowerCase().contains("juros") || conta.getNome().toLowerCase().contains("financeir"))
            return "Despesas Financeiras";
        return "Outras Despesas";
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

    private void logContasPorGrupo(Map<String, List<Contas>> contasPorSubclasse) {
        for (Map.Entry<String, List<Contas>> entry : contasPorSubclasse.entrySet()) {
            System.out.println("Grupo: " + entry.getKey());
            for (Contas conta : entry.getValue()) {
                System.out.println("  - " + conta.getConta() + " - " + conta.getNome());
            }
        }
    }
}
