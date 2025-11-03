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
public class CalcularSegundaParcela13Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularSegundaParcela13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularSegundaParcela13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioBase = folha.getSalarioBase();
            LocalDate dataAdmissao = folha.getDataAdmissao();
            BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();
            int numeroDependentes = folha.getDependentesIrrf();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoSegundaParcela13 = calculoBaseService.calcularSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao, numeroDependentes, horasTrabalhadasPorMes);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoSegundaParcela13);

            logger.info("2ª parcela 13º calculada para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular 2ª parcela 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Segunda Parcela do 13°: " + e.getMessage());
        }

        return resultado;
    }
}
