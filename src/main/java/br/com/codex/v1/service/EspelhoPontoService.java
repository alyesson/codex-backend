package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.EspelhoPontoDto;
import br.com.codex.v1.domain.dto.InconsistenciaPontoDto;
import br.com.codex.v1.domain.dto.ProcessamentoFolhaDto;
import br.com.codex.v1.domain.dto.TotaisFolhaPontoDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.repository.CadastroJornadaRepository;
import br.com.codex.v1.domain.repository.EspelhoPontoRepository;
import br.com.codex.v1.domain.repository.RegistroPontoRepository;
import br.com.codex.v1.domain.rh.*;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EspelhoPontoService {
    private static final Logger logger = LoggerFactory.getLogger(EspelhoPontoService.class);


    @Autowired
    private EspelhoPontoRepository espelhoPontoRepository;

    @Autowired
    private CadastroColaboradoresRepository colaboradoresRepository;

    @Autowired
    private CadastroJornadaRepository jornadaRepository;

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    // MÉTODOS AUXILIARES QUE ESTAVAM FALTANDO

    private List<RegistroPonto> lerArquivoPonto(MultipartFile arquivo) throws IOException {
        List<RegistroPonto> registros = new ArrayList<>();
        String loteImportacao = "LOTE_" + System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    RegistroPonto registro = new RegistroPonto(linha, loteImportacao);
                    if (registro.getData() != null) {
                        // Buscar colaborador pelo PIS
                        if (registro.getPis() != null) {
                            CadastroColaboradores colaborador = colaboradoresRepository.findByNumeroPis(registro.getPis());
                            registro.setColaborador(colaborador);
                        }
                        registros.add(registro);
                    }
                }
            }
        }

        return registros;
    }

    private Map<String, Map<LocalDate, List<RegistroPonto>>> agruparRegistros(List<RegistroPonto> registros,
                                                                              LocalDate dataInicio,
                                                                              LocalDate dataFim) {
        return registros.stream()
                .filter(r -> r.getPis() != null && r.getData() != null)
                .filter(r -> !r.getData().isBefore(dataInicio) && !r.getData().isAfter(dataFim))
                .collect(Collectors.groupingBy(
                        RegistroPonto::getPis,
                        Collectors.groupingBy(RegistroPonto::getData)
                ));
    }

    private List<InconsistenciaPontoDto> identificarInconsistencias(LocalDate dataInicio, LocalDate dataFim) {
        List<InconsistenciaPontoDto> inconsistencias = new ArrayList<>();

        // Buscar todos os espelhos do período
        List<EspelhoPonto> espelhos = espelhoPontoRepository.findByPeriodo(dataInicio, dataFim);

        for (EspelhoPonto espelho : espelhos) {
            // Verificar horários faltantes
            if (espelho.getEntrada() == null || espelho.getSaida() == null) {
                inconsistencias.add(new InconsistenciaPontoDto(
                        "HORARIO_INCOMPLETO",
                        "Registro de ponto incompleto para o dia",
                        espelho.getData(),
                        espelho.getColaborador().getNomeColaborador(),
                        espelho.getColaborador().getNumeroPis(),
                        "ALTA"
                ));
            }

            // Verificar horas trabalhadas muito baixas
            if (espelho.getHorasTrabalhadasMinutos() != null && espelho.getHorasTrabalhadasMinutos() < 60) {
                inconsistencias.add(new InconsistenciaPontoDto(
                        "HORAS_TRABALHADAS_BAIXAS",
                        "Horas trabalhadas abaixo do esperado: " + espelho.getHorasTrabalhadasMinutos() + " minutos",
                        espelho.getData(),
                        espelho.getColaborador().getNomeColaborador(),
                        espelho.getColaborador().getNumeroPis(),
                        "MEDIA"
                ));
            }

            // Verificar muitas horas extras
            if (espelho.getHorasExtrasMinutos() != null && espelho.getHorasExtrasMinutos() > 480) { // 8 horas
                inconsistencias.add(new InconsistenciaPontoDto(
                        "HORAS_EXTRAS_ELEVADAS",
                        "Horas extras acima do limite: " + espelho.getHorasExtrasMinutos() + " minutos",
                        espelho.getData(),
                        espelho.getColaborador().getNomeColaborador(),
                        espelho.getColaborador().getNumeroPis(),
                        "MEDIA"
                ));
            }
        }

        return inconsistencias;
    }

    private void calcularTotaisGerais(ProcessamentoFolhaDto resultado, List<TotaisFolhaPontoDto> totais) {
        Long totalHorasTrabalhadas = 0L;
        Long totalHorasExtras = 0L;
        Long totalHorasFaltantes = 0L;
        BigDecimal totalCusto = BigDecimal.ZERO;

        for (TotaisFolhaPontoDto total : totais) {
            totalHorasTrabalhadas += total.getTotalHorasTrabalhadasMinutos();
            totalHorasExtras += total.getTotalHorasExtrasMinutos();
            totalHorasFaltantes += total.getTotalHorasFaltantesMinutos();
            totalCusto = totalCusto.add(total.getTotalCustoHorasExtras());
        }

        resultado.setTotalHorasTrabalhadasMinutos(totalHorasTrabalhadas);
        resultado.setTotalHorasExtrasMinutos(totalHorasExtras);
        resultado.setTotalHorasFaltantesMinutos(totalHorasFaltantes);
        resultado.setTotalCustoHorasExtras(totalCusto);
        resultado.setTotalColaboradoresProcessados(totais.size());
    }

    // PROCESSAR FOLHA PONTO - MÉTUDO PRINCIPAL
    public ProcessamentoFolhaDto processarFolhaPonto(LocalDate dataInicio, LocalDate dataFim) {
        ProcessamentoFolhaDto resultado = new ProcessamentoFolhaDto();
        resultado.setDataInicio(dataInicio);
        resultado.setDataFim(dataFim);
        resultado.setDataProcessamento(LocalDateTime.now());

        try {
            // Buscar totais consolidados
            List<TotaisFolhaPontoDto> totais = espelhoPontoRepository.findTotaisPorColaboradorNoPeriodo(dataInicio, dataFim);
            resultado.setTotaisColaboradores(totais);

            // Calcular totais gerais
            calcularTotaisGerais(resultado, totais);

            // Gerar relatório de inconsistências
            List<InconsistenciaPontoDto> inconsistencias = identificarInconsistencias(dataInicio, dataFim);
            resultado.setInconsistencias(inconsistencias);

            resultado.setSucesso(true);
            resultado.setMensagem("Folha ponto processada com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao processar folha ponto", e);
            resultado.setSucesso(false);
            resultado.setMensagem("Erro ao processar folha ponto: " + e.getMessage());
        }

        return resultado;
    }

    // IMPORTAR E PROCESSAR ARQUIVO DE PONTO
    public ImportacaoPontoResultado importarArquivoPonto(MultipartFile arquivo, LocalDate dataInicio, LocalDate dataFim) {
        ImportacaoPontoResultado resultado = new ImportacaoPontoResultado();
        resultado.setDataInicio(dataInicio);
        resultado.setDataFim(dataFim);
        resultado.setDataImportacao(LocalDateTime.now());
        resultado.setNomeArquivo(arquivo.getOriginalFilename());

        List<EspelhoPonto> espelhosProcessados = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        try {
            // Ler arquivo
            List<RegistroPonto> registros = lerArquivoPonto(arquivo);
            resultado.setTotalRegistros(registros.size());

            // Agrupar por PIS e data
            Map<String, Map<LocalDate, List<RegistroPonto>>> registrosAgrupados = agruparRegistros(registros, dataInicio, dataFim);

            // Processar cada colaborador
            for (Map.Entry<String, Map<LocalDate, List<RegistroPonto>>> entry : registrosAgrupados.entrySet()) {
                String pis = entry.getKey();
                Map<LocalDate, List<RegistroPonto>> registrosPorData = entry.getValue();

                try {
                    CadastroColaboradores colaborador = colaboradoresRepository.findByNumeroPis(pis);
                    if (colaborador == null) {
                        erros.add("Colaborador com PIS " + pis + " não encontrado");
                        continue;
                    }

                    CadastroJornada jornada = jornadaRepository.findFirstByCodigoJornada(colaborador.getJornada());
                    if (jornada == null) {
                        erros.add("Jornada " + colaborador.getJornada() + " não encontrada para colaborador " + colaborador.getNomeColaborador());
                        continue;
                    }

                    // Processar cada dia
                    for (Map.Entry<LocalDate, List<RegistroPonto>> diaEntry : registrosPorData.entrySet()) {
                        LocalDate data = diaEntry.getKey();
                        List<RegistroPonto> registrosDia = diaEntry.getValue();

                        // Verificar se já existe
                        if (!espelhoPontoRepository.existsByColaboradorAndData(colaborador.getId(), data)) {
                            EspelhoPonto espelho = processarDia(colaborador, jornada, data, registrosDia);
                            espelhosProcessados.add(espelho);
                        }
                    }

                } catch (Exception e) {
                    erros.add("Erro ao processar colaborador PIS " + pis + ": " + e.getMessage());
                }
            }

            // Salvar no banco
            if (!espelhosProcessados.isEmpty()) {
                espelhoPontoRepository.saveAll(espelhosProcessados);
            }

            resultado.setEspelhosProcessados(espelhosProcessados.size());
            resultado.setErros(erros);
            resultado.setSucesso(erros.isEmpty());

        } catch (Exception e) {
            logger.error("Erro na importação do arquivo de ponto", e);
            erros.add("Erro na importação: " + e.getMessage());
            resultado.setErros(erros);
            resultado.setSucesso(false);
        }

        return resultado;
    }

    // PROCESSAR UM DIA ESPECÍFICO
    private EspelhoPonto processarDia(CadastroColaboradores colaborador, CadastroJornada jornada,
                                      LocalDate data, List<RegistroPonto> registros) {

        EspelhoPonto espelho = new EspelhoPonto();
        espelho.setColaborador(colaborador);
        espelho.setJornada(jornada);
        espelho.setData(data);
        espelho.setSituacao(Situacao.ATIVO);

        // Ordenar registros por hora
        List<RegistroPonto> registrosOrdenados = registros.stream()
                .sorted(Comparator.comparing(RegistroPonto::getHora))
                .collect(Collectors.toList());

        // Identificar horários
        if (registrosOrdenados.size() >= 2) {
            espelho.setEntrada(registrosOrdenados.get(0).getHora());
            espelho.setSaida(registrosOrdenados.get(registrosOrdenados.size() - 1).getHora());

            if (registrosOrdenados.size() >= 4) {
                espelho.setSaidaAlmoco(registrosOrdenados.get(1).getHora());
                espelho.setRetornoAlmoco(registrosOrdenados.get(2).getHora());
            }
        }

        // CALCULAR HORAS TRABALHADAS
        calcularHorasTrabalhadas(espelho);

        // CALCULAR HORAS EXTRAS/FALTANTES
        calcularHorasExtrasFaltantes(espelho, jornada);

        // CALCULAR CUSTO HORAS EXTRAS
        calcularCustoHorasExtras(espelho, colaborador);

        return espelho;
    }

    private void calcularHorasTrabalhadas(EspelhoPonto espelho) {
        if (espelho.getEntrada() != null && espelho.getSaida() != null) {
            Duration horasTotais = Duration.between(espelho.getEntrada(), espelho.getSaida());

            // Subtrair intervalo de almoço
            if (espelho.getSaidaAlmoco() != null && espelho.getRetornoAlmoco() != null) {
                Duration intervaloAlmoco = Duration.between(espelho.getSaidaAlmoco(), espelho.getRetornoAlmoco());
                horasTotais = horasTotais.minus(intervaloAlmoco);
            }

            espelho.setHorasTrabalhadas(horasTotais);
        }
    }

    private void calcularHorasExtrasFaltantes(EspelhoPonto espelho, CadastroJornada jornada) {
        if (espelho.getHorasTrabalhadas() != null && jornada.getInicioExpediente() != null && jornada.getFimExpediente() != null) {
            // Calcular jornada prevista
            Duration jornadaPrevista = Duration.between(jornada.getInicioExpediente(), jornada.getFimExpediente());

            // Subtrair intervalo de almoço da jornada
            if (jornada.getInicioAlmoco() != null && jornada.getFimAlmoco() != null) {
                Duration intervaloJornada = Duration.between(jornada.getInicioAlmoco(), jornada.getFimAlmoco());
                jornadaPrevista = jornadaPrevista.minus(intervaloJornada);
            }

            espelho.setHorasDeveriaTrabalhar(jornadaPrevista);

            // Comparar com horas trabalhadas
            if (espelho.getHorasTrabalhadas().compareTo(jornadaPrevista) > 0) {
                espelho.setHorasExtras(espelho.getHorasTrabalhadas().minus(jornadaPrevista));
            } else {
                espelho.setHorasFaltantes(jornadaPrevista.minus(espelho.getHorasTrabalhadas()));
            }
        }
    }

    private void calcularCustoHorasExtras(EspelhoPonto espelho, CadastroColaboradores colaborador) {
        if (espelho.getHorasExtras().toMinutes() > 0 && colaborador.getSalarioHora() != null) {
            BigDecimal minutosExtras = BigDecimal.valueOf(espelho.getHorasExtras().toMinutes());
            BigDecimal valorMinuto = colaborador.getSalarioHora().divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);

            // Acréscimo de 50% para horas extras
            BigDecimal acrescimo = BigDecimal.valueOf(1.5);
            BigDecimal custo = valorMinuto.multiply(minutosExtras).multiply(acrescimo);

            espelho.setCustoHorasExtras(custo.setScale(2, RoundingMode.HALF_UP));
        }
    }

    // BUSCAR ESPELHO PONTO
    public List<EspelhoPontoDto> buscarEspelhoPonto(LocalDate dataInicio, LocalDate dataFim, Long colaboradorId) {
        List<EspelhoPonto> espelhos;

        if (colaboradorId != null) {
            espelhos = espelhoPontoRepository.findByColaboradorAndPeriodo(colaboradorId, dataInicio, dataFim);
        } else {
            espelhos = espelhoPontoRepository.findByPeriodo(dataInicio, dataFim);
        }

        return espelhos.stream().map(EspelhoPontoDto::new).collect(Collectors.toList());
    }

    // MÉTODOS AUXILIARES (lerArquivoPonto, agruparRegistros, identificarInconsistencias, calcularTotaisGerais)
    // ... implementação similar à anterior
}