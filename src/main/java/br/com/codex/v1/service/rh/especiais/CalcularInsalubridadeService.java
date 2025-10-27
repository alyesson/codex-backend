package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import br.com.codex.v1.service.TabelaImpostoRendaService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularInsalubridadeService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularInsalubridadeService(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
        BigDecimal valorSalarioMinimo = obtemSalarioMinimo();

        BigDecimal porcentagemInsalubre = folha.getInsalubridade();
        BigDecimal valorInsalubre = (valorSalarioMinimo.multiply(porcentagemInsalubre)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
        resultado.put("referencia", porcentagemInsalubre);
        resultado.put("vencimentos", valorInsalubre);
        resultado.put("descontos", BigDecimal.ZERO);

        return resultado;
    }

    private BigDecimal obtemSalarioMinimo(){
        return tabelaImpostoRendaService.getSalarioMinimo();
    }
}
