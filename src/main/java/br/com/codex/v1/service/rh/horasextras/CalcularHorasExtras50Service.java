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
public class CalcularHorasExtras50Service {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Setter
    String numeroMatricula;

    private BigDecimal obtemSalarioMinimo() {
        return tabelaImpostoRendaService.getSalarioMinimo();
    }

    public Map<String, BigDecimal> calcularHorasExtras50() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal percentualInsalubridade = folha.getInsalubridade();
            BigDecimal totalHoraExtra50 = folha.getHorasExtras50();
            BigDecimal salarioPorHora = folha.getSalarioHora();

            // ✅ Validação de horas extras
            if (totalHoraExtra50 == null || totalHoraExtra50.compareTo(BigDecimal.ZERO) <= 0) {
                return resultado;
            }

            BigDecimal valorHoraExtra50Mes;

            if (percentualInsalubridade == null || percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {
                valorHoraExtra50Mes = salarioPorHora.multiply(new BigDecimal("1.5"))
                        .multiply(totalHoraExtra50)
                        .setScale(2, RoundingMode.HALF_UP);

            } else {
                BigDecimal valorSalarioMinimo = obtemSalarioMinimo();
                BigDecimal horasTrabNoMes = folha.getHorasMes();

                BigDecimal valorInsalubre = valorSalarioMinimo.multiply(percentualInsalubridade)
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                        .divide(horasTrabNoMes, 2, RoundingMode.HALF_UP);

                valorHoraExtra50Mes = salarioPorHora.add(valorInsalubre).multiply(new BigDecimal("1.5"))
                        .multiply(totalHoraExtra50).setScale(2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", totalHoraExtra50);
            resultado.put("vencimentos", valorHoraExtra50Mes);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular horas extras 50%: " + e.getMessage());
        }

        return resultado;
    }
}