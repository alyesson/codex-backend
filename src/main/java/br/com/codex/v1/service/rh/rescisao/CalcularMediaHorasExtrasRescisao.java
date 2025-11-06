package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CalcularMediaHorasExtrasRescisao {

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    String numeroMatricula;

    public BigDecimal calcularMediaHorasExtrasRescisao(LocalDate dataRescisao) {
        LocalDate dataInicio = dataRescisao.minusMonths(12);

        // Busca total de horas extras dos últimos 12 meses
        BigDecimal totalHorasExtras50 = folhaMensalEventosCalculadaRepository.findTotalHorasExtras50Periodo(numeroMatricula, 98, dataInicio, dataRescisao);
        BigDecimal totalHorasExtras70 = folhaMensalEventosCalculadaRepository.findTotalHorasExtras70Periodo(numeroMatricula, 99, dataInicio, dataRescisao);
        BigDecimal totalHorasExtras100 = folhaMensalEventosCalculadaRepository.findTotalHorasExtras100Periodo(numeroMatricula, 100, dataInicio, dataRescisao);
        BigDecimal totalHorasExtras = totalHorasExtras50.add(totalHorasExtras70).add(totalHorasExtras100);

        // Calcula média
        BigDecimal mediaHorasExtras = totalHorasExtras.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        return mediaHorasExtras;
    }
}
