package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularAdiantamentoSalario40PorcentoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    public Map<String, BigDecimal>calculaAdiantamentoSalarial40Porcento(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
        BigDecimal salarioBase = folha.getSalarioBase();

        BigDecimal adiantamentoSalarial = (salarioBase.multiply(new BigDecimal("40"))).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        resultado.put("referencia", new BigDecimal(40));
        resultado.put("vencimentos", adiantamentoSalarial);
        resultado.put("descontos", BigDecimal.ZERO);

        return resultado;
    }
}
