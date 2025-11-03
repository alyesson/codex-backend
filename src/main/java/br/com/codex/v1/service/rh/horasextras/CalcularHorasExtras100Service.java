package br.com.codex.v1.service.rh.horasextras;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.TabelaImpostoRendaService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularHorasExtras100Service {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Setter
    String numeroMatricula;

    private BigDecimal obtemSalarioMinimo() {
        return tabelaImpostoRendaService.getSalarioMinimo();
    }

    public Map<String, BigDecimal> calcularHorasExtras100() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal percentualInsalubridade = folha.getInsalubridade();
            BigDecimal totalHoraExtra100 = folha.getHorasExtras100();
            BigDecimal salarioPorHora = folha.getSalarioHora();

            if (totalHoraExtra100 == null || totalHoraExtra100.compareTo(BigDecimal.ZERO) <= 0) {
                return resultado;
            }

            BigDecimal valorHoraExtra100Mes;

            if (percentualInsalubridade == null || percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {
                    valorHoraExtra100Mes = salarioPorHora.multiply(new BigDecimal("2"))
                        .multiply(totalHoraExtra100)
                        .setScale(2, RoundingMode.HALF_UP);
            } else {
                BigDecimal valorSalarioMinimo = obtemSalarioMinimo();
                BigDecimal horasTrabNoMes = folha.getHorasMes();

                BigDecimal valorInsalubre = valorSalarioMinimo.multiply(percentualInsalubridade).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                        .divide(horasTrabNoMes, 2, RoundingMode.HALF_UP);

                valorHoraExtra100Mes = salarioPorHora.add(valorInsalubre)
                        .multiply(new BigDecimal("2")).multiply(totalHoraExtra100).setScale(2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", totalHoraExtra100);
            resultado.put("vencimentos", valorHoraExtra100Mes);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular horas extras 100%: " + e.getMessage());
        }
        return resultado;
    }
}
