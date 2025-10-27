package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import br.com.codex.v1.service.rh.horarios.CalcularHorasNormaisDiurnasService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularQuebraCaixaService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private CalcularHorasNormaisDiurnasService calculaHorasNormaisDiurnasService;

    @Setter
    String numeroMatricula;

    BigDecimal valorReferenteHoraDiurna;

    public Map<String, BigDecimal> calcularQuebraCaixa() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorQuebraCaixa = folha.getQuebraCaixa();
            BigDecimal horasPorMes = folha.getHorasMes();

            //Calculando o valor da Hora Diurna
            calculaHorasNormaisDiurnasService.calculaHorasNormaisDiurnas();

            BigDecimal quebCaixa = (valorQuebraCaixa.multiply(valorReferenteHoraDiurna)).divide(horasPorMes,2, RoundingMode.HALF_UP);

            resultado.put("referencia", valorQuebraCaixa);
            resultado.put("vencimentos", quebCaixa);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular comissão: " + e.getMessage());
        }
        return resultado;
    }
}
