package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularFgtsDepositadoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFgtsDepositadoService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularFgtsDepositado() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal fgts = rescisao.getValorFgts();

            resultado.put("referencia", BigDecimal.ZERO);
            resultado.put("vencimentos", fgts);
            resultado.put("descontos", BigDecimal.ZERO);

        }catch (Exception e){
            logger.error("Erro ao informar o FGTS para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao informar o FGTS: " + e.getMessage());
        }
        return resultado;
    }
}
