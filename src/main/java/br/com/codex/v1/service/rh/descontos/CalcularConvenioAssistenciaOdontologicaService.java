package br.com.codex.v1.service.rh.descontos;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.decimoterceiro.CalcularIrrfDecimoTerceiroService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularConvenioAssistenciaOdontologicaService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularConvenioAssistenciaOdontologicaService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularConvenioAssistenciaOdontologica() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try{
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal planoOdonto = folha.getValorPlanoOdonto();

            resultado.put("referencia", planoOdonto);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", planoOdonto);

        }catch (Exception e) {
            logger.error("Erro ao calcular o convênio assistência odontológica para matrícula {}", numeroMatricula, e);
        }
        return resultado;
    }

}
