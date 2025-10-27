package br.com.codex.v1.service.rh.horasextras;

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
public class CalcularComplementoMediaHE7013Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularComplementoMediaHE7013Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularComplementoMediaHE7013() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioBase = folha.getSalarioBase();
            LocalDate dataAdmissao = folha.getDataAdmissao();
            BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoComplementoHE7013 = calculoBaseService.calcularComplementoMediaHE7013(numeroMatricula, salarioBase, dataAdmissao, horasTrabalhadasPorMes);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoComplementoHE7013);

            logger.info("Complemento média HE 70% 13º calculado para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular complemento média HE 70% 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular complemento média HE 70% do 13º: " + e.getMessage());
        }

        return resultado;
    }
}
