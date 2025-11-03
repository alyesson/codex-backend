package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularGratificacaoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularGratificacao() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorGratifica = folha.getGratificacao();

            if (valorGratifica == null || valorGratifica.compareTo(BigDecimal.ZERO) <= 0) {
                return resultado;
            }

            resultado.put("referencia", valorGratifica);
            resultado.put("vencimentos", valorGratifica);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular gratificação: " + e.getMessage());
        }
        return resultado;
    }
}
