package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CalcularPremioFrequenciaService {
    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    public Map<String, BigDecimal> calcularPremioFrequencia(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
        BigDecimal valorPremio = folha.getGratificacao();
        resultado.put("referencia", valorPremio);
        resultado.put("vencimentos", valorPremio);


        return resultado;
    }
}
