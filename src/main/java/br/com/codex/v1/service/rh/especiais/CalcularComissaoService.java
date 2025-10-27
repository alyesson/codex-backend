package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularComissaoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularComissao() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal percentualComissao = folha.getComissao();
            BigDecimal vendasMes = folha.getValorVendaMes();

            BigDecimal valorComissao = BigDecimal.ZERO;

            if (percentualComissao != null && vendasMes != null && percentualComissao.compareTo(BigDecimal.ZERO) > 0 &&
                    vendasMes.compareTo(BigDecimal.ZERO) > 0) {

                valorComissao = percentualComissao.multiply(vendasMes).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", vendasMes != null ? vendasMes : BigDecimal.ZERO);
            resultado.put("vencimentos", valorComissao);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular comissão: " + e.getMessage());
        }
        return resultado;
    }
}
