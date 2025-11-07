package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CalcularInsalubridadeRescisaoService {

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    private String numeroMatricula;

    private static final Integer CODIGO_INSALUBRIDADE = 46;

    public BigDecimal calcularSomaInsalubridade12Meses(LocalDate dataRescisao, LocalDate dataAdmissao) {
        try {
            LocalDate dataInicio = dataRescisao.minusMonths(12);

            // Se admitido há menos de 1 ano, ajusta a data início
            if (dataAdmissao.isAfter(dataInicio)) {
                dataInicio = dataAdmissao;
            }

            // **SOMA (não média) dos valores de insalubridade**
            BigDecimal somaInsalubridade = folhaMensalEventosCalculadaRepository.findSomaInsalubridadePeriodo(numeroMatricula, CODIGO_INSALUBRIDADE, dataInicio, dataRescisao);
            return somaInsalubridade;

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
