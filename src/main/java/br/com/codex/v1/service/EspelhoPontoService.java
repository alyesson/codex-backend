package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.EspelhoPontoDto;
import br.com.codex.v1.domain.dto.InconsistenciaPontoDto;
import br.com.codex.v1.domain.dto.ProcessamentoFolhaDto;
import br.com.codex.v1.domain.dto.TotaisFolhaPontoDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.repository.CadastroJornadaRepository;
import br.com.codex.v1.domain.repository.EspelhoPontoRepository;
import br.com.codex.v1.domain.rh.*;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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

    public List<EspelhoPontoDto> buscarEspelhoPonto(LocalDate dataInicio, LocalDate dataFim, Long colaboradorId) {
        List<EspelhoPonto> espelhos;

        if (colaboradorId != null) {
            espelhos = espelhoPontoRepository.findByColaboradorAndPeriodo(colaboradorId, dataInicio, dataFim);
        } else {
            espelhos = espelhoPontoRepository.findByPeriodo(dataInicio, dataFim);
        }

        return espelhos.stream().map(EspelhoPontoDto::new).collect(Collectors.toList());
    }
}