package br.com.codex.v1.service.rh.decimoterceiro;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
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
public class CalcularDecimoTerceiroComMediaComissoesService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDecimoTerceiroComMediaComissoesService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularDecimoTerceiroComMediaComissoes() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            LocalDate dataAdmissao = folha.getDataAdmissao();
            BigDecimal salarioBase = folha.getSalarioBase();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultado13Comissoes = calculoBaseService.calcularDecimoTerceiroComMediaComissoes(numeroMatricula, salarioBase, dataAdmissao);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultado13Comissoes);

            logger.info("13º com média comissões calculado para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular 13º com média comissões para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 13º com média de comissões: " + e.getMessage());
        }

        return resultado;
    }
}
