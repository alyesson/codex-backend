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
public class CalcularHorasExtras70Service {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Setter
    String numeroMatricula;

    private BigDecimal obtemSalarioMinimo() {
        return tabelaImpostoRendaService.getSalarioMinimo();
    }

    public Map<String, BigDecimal> calcularHorasExtras70() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal percentualInsalubridade = folha.getInsalubridade();
            BigDecimal totalHoraExtra70 = folha.getHorasExtras70();
            BigDecimal salarioPorHora = folha.getSalarioHora();

            if (totalHoraExtra70 == null || totalHoraExtra70.compareTo(BigDecimal.ZERO) <= 0) {
                return resultado;
            }

            BigDecimal valorHoraExtra70Mes;

            if (percentualInsalubridade == null || percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {
                valorHoraExtra70Mes = salarioPorHora.multiply(new BigDecimal("1.7")).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
            } else {
                BigDecimal valorSalarioMinimo = obtemSalarioMinimo();
                BigDecimal horasTrabNoMes = folha.getHorasMes();

                BigDecimal valorInsalubre = valorSalarioMinimo.multiply(percentualInsalubridade).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                        .divide(horasTrabNoMes, 2, RoundingMode.HALF_UP);

                valorHoraExtra70Mes = salarioPorHora.add(valorInsalubre).multiply(new BigDecimal("1.7"))
                        .multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", totalHoraExtra70);
            resultado.put("vencimentos", valorHoraExtra70Mes);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular horas extras 70%: " + e.getMessage());
        }
        return resultado;
    }
}
