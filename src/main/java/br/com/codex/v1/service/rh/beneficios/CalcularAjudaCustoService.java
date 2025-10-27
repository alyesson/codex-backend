package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularAjudaCustoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    public Map<String, BigDecimal> calcularAjudaCusto(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        if(tipoSalario.equals("Quinzenal")) {
            FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
            BigDecimal valorReferencia = folha.getEventos().stream()
                    .filter(evento -> evento.getCodigoEvento().equals(130))
                    .findFirst()
                    .map(evento -> {
                        try { return new BigDecimal(evento.getReferencia()); }
                        catch (NumberFormatException e) { return BigDecimal.ZERO; }
                    })
                    .orElse(BigDecimal.ZERO);

            BigDecimal valorAjudaCusto = valorReferencia.multiply(folha.getTransporteDia());
            resultado.put("referencia", valorReferencia);
            resultado.put("vencimentos", valorAjudaCusto);
        } else {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorVT = folha.getValorValeTransporte();
            resultado.put("referencia", valorVT);
            resultado.put("vencimentos", valorVT);
        }

        return resultado;
    }
}
