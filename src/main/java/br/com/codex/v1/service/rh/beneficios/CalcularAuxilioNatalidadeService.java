package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularAuxilioNatalidadeService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularAuxilioNatalidade(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
        BigDecimal valorAjudaCusto = folha.getSalarioBase();
        resultado.put("referencia", valorAjudaCusto);
        resultado.put("vencimentos", valorAjudaCusto);

        return resultado;
    }
}
