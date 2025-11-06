package br.com.codex.v1.service.rh.descontos;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularDescontoValeTransporteService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDescontoValeTransporteService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularDescontoValeTransporte() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal descontoValeTransporte = folha.getSalarioBase().multiply(new BigDecimal("0.06")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(6));
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", descontoValeTransporte);

        }catch (Exception e){
            logger.error("Erro ao calcular o desconto do vale transporte para matrícula {}", numeroMatricula, e);
        }
        return resultado;
    }
}
