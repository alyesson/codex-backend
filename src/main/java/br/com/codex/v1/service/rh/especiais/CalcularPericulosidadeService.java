package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularPericulosidadeService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularPericulosidade() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioBase = folha.getSalarioBase();
            BigDecimal porcentagemPericuloso = folha.getPericulosidade();

            if (porcentagemPericuloso == null || porcentagemPericuloso.compareTo(BigDecimal.ZERO) <= 0) {
                return resultado;
            }

            BigDecimal valorPericuloso = salarioBase.multiply(porcentagemPericuloso).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            resultado.put("referencia", porcentagemPericuloso);
            resultado.put("vencimentos", valorPericuloso);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular periculosidade: " + e.getMessage());
        }
        return resultado;
    }
}