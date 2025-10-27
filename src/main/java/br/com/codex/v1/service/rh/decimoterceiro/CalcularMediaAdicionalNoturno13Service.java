package br.com.codex.v1.service.rh.decimoterceiro;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularMediaAdicionalNoturno13Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaAdicionalNoturno13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaAdicionalNoturno13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            LocalDate dataAdmissao = folha.getDataAdmissao();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoAdicionalNoturno = calculoBaseService.calcularMediaAdicionalNoturno13(numeroMatricula, dataAdmissao);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoAdicionalNoturno);

            logger.info("Média adicional noturno 13º calculado para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular média adicional noturno 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular média adicional noturno do 13º: " + e.getMessage());
        }

        return resultado;
    }
}
