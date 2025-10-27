package br.com.codex.v1.service.rh.impostos;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularFgts13salarioService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFgts13salarioService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularFGTS13salario() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            // ✅ Buscar dados do funcionário
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            String descricaoDoCargo = folha.getCargoFuncionario();
            BigDecimal salarioBase = folha.getSalarioBase();
            LocalDate dataAdmissao = folha.getDataAdmissao();

            BigDecimal taxaFGTS;

            // ✅ Definir taxa conforme tipo de contrato
            if (descricaoDoCargo != null && descricaoDoCargo.toLowerCase().contains("aprendiz")) {
                taxaFGTS = new BigDecimal("0.02");
            } else {
                taxaFGTS = new BigDecimal("0.08");
            }

            // ✅ Calcular meses trabalhados
            int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao, LocalDate.now());

            // ✅ Cálculo do 13º (integral ou proporcional)
            BigDecimal decimoTerceiroIntegral = salarioBase.multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // ✅ Calcular FGTS sobre o 13º
            BigDecimal valorFGTS13 = decimoTerceiroIntegral.multiply(taxaFGTS).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados)); // Meses como referência
            resultado.put("vencimentos", valorFGTS13);

            logger.info("FGTS sobre 13º calculado para {}: {} meses = R$ {}", numeroMatricula, mesesTrabalhados, valorFGTS13);

        } catch (Exception e) {
            logger.error("Erro ao calcular FGTS sobre 13º salário para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular FGTS sobre 13º salário: " + e.getMessage());
        }

        return resultado;
    }
}
