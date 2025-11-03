package br.com.codex.v1.service.rh.decimoterceiro;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.CalculosAuxiliaresFolha;
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
public class CalcularPrimeiraParcela13Service {

    private static final Logger logger = LoggerFactory.getLogger(CalcularPrimeiraParcela13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    public Map<String, BigDecimal> calcularPrimeiraParcela13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            BigDecimal salarioBase;

            // ✅ Obter salário base conforme tipo de salário
            if (tipoSalario.equals("Quinzenal")) {
                FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
                salarioBase = folha.getSalarioBase();
            } else {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                salarioBase = folha.getSalarioBase();
            }

            // ✅ Obter data de admissão
            LocalDate dataAdmissao = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();

            // ✅ Calcular meses trabalhados considerando a regra dos 15 dias
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, LocalDate.now());

            // ✅ Cálculo proporcional do 13º
            BigDecimal valorMensal = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            BigDecimal valorProporcional = valorMensal.multiply(new BigDecimal(mesesTrabalhados));
            BigDecimal primeiraParcela = valorProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", primeiraParcela);

            logger.info("1ª parcela 13º calculada para {}: {} meses = R$ {}", numeroMatricula, mesesTrabalhados, primeiraParcela);

        } catch (Exception e) {
            logger.error("Erro ao calcular primeira parcela do 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular primeira parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }
}