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
public class CalcularPericulosidadePrimeiraParcela13Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularPericulosidadePrimeiraParcela13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularPericulosidadePrimeiraParcela13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            LocalDate dataAdmissao = folha.getDataAdmissao();
            BigDecimal salarioBase = folha.getSalarioBase();

            LocalDate hoje = LocalDate.now();
            int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoPericulosidade = calculoBaseService.calcularPericulosidadePrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoPericulosidade);

            logger.info("Periculosidade 1ª parcela 13º calculada para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular periculosidade 1ª parcela 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Periculosidade Sobre 1° Parcela do 13°: " + e.getMessage());
        }

        return resultado;
    }
}
