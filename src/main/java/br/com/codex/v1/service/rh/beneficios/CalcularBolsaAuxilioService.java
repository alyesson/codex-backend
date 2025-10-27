package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularBolsaAuxilioService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    public Map<String, BigDecimal> calcularBolsaAuxilio(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        BigDecimal bolsaAuxilio;

        try {
            if(tipoSalario.equals("Quinzenal")){
                FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
                bolsaAuxilio = folha.getSalarioBase();
            }else{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                bolsaAuxilio = folha.getSalarioBase();
            }

            if (bolsaAuxilio == null || bolsaAuxilio.compareTo(BigDecimal.ZERO) <= 0) {
                bolsaAuxilio = BigDecimal.ZERO;
            }

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", bolsaAuxilio);
            return resultado;

        } catch (Exception e) {
            throw new DataIntegrityViolationException("Erro ao calcular bolsa auxílio: " +e);
        }
    }
}
