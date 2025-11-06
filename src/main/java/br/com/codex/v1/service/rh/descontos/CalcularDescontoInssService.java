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
public class CalcularDescontoInssService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDescontoInssService.class);

    @Autowired
    CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularDescontoInss(){
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
        BigDecimal salarioBase = folha.getSalarioBase();

        try{
            BigDecimal descontoInss = calculoBaseService.calcularINSS(salarioBase);
            resultado.put("referencia", descontoInss);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", descontoInss);

        }catch (Exception e){
            logger.error("Erro ao calcular o desconto do inss para matrícula {}", numeroMatricula, e);
        }
        return resultado;
    }
}
