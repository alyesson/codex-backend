package br.com.codex.v1.service.rh.horasextras;

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
public class CalcularComplementoMediaHE10013Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularComplementoMediaHE10013Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularComplementoMediaHE10013() {
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
            Map<String, BigDecimal> resultadoComplementoHE10013 = calculoBaseService.calcularComplementoMediaHE10013(numeroMatricula, salarioBase, dataAdmissao, horasTrabalhadasPorMes);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoComplementoHE10013);

            logger.info("Complemento média HE 100% 13º calculado para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular complemento média HE 100% 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular complemento média HE 100% do 13º: " + e.getMessage());
        }

        return resultado;
    }
}
