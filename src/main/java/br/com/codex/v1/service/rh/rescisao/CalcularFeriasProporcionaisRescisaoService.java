package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.service.CadastroColaboradoresService;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalcularFeriasProporcionaisRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFeriasProporcionaisRescisaoService.class);

    @Autowired
    private CalcularMediaHorasExtrasRescisao calcularMediaHorasExtrasRescisao;

    @Autowired
    private CalcularInsalubridadeRescisaoService calcularInsalubridadeRescisaoService;

    @Autowired
    private CadastroColaboradoresRepository cadastroColaboradoresRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularFeriasProporcionaisRescisao(LocalDate dataRescisao, LocalDate dataAdmissao) {

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {

            // 1. Busca salário base do colaborador
            CadastroColaboradores colaborador = findByMatricula(numeroMatricula);
            BigDecimal salarioBase = colaborador.getSalarioColaborador();

            // 2. Calcula meses trabalhados no período aquisitivo
            long mesesTrabalhados = calcularMesesProporcionais(dataAdmissao, dataRescisao);

            // 3. Calcula média das horas extras (CORRIGIDO)
            calcularMediaHorasExtrasRescisao.setNumeroMatricula(numeroMatricula);
            BigDecimal mediaHorasExtras = calcularMediaHorasExtrasRescisao.calcularMediaHorasExtrasRescisao(dataRescisao);

            // 4. Calcula valor da hora extra
            BigDecimal salarioHora = calcularSalarioHora(salarioBase); // salarioBase ÷ 220
            BigDecimal valorHoraExtra = calcularValorHoraExtra(salarioHora);

            // 5. Valor das horas extras nas férias
            BigDecimal valorHorasExtrasFerias = mediaHorasExtras.multiply(valorHoraExtra);

            // 6. Base de cálculo das férias
            BigDecimal baseCalculo = salarioBase.add(valorHorasExtrasFerias);

            // 7. Férias proporcionais
            BigDecimal feriasProporcionais = baseCalculo.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(mesesTrabalhados));

            resultado.put("referencia", BigDecimal.valueOf(mesesTrabalhados));
            resultado.put("vencimentos", feriasProporcionais);

        }catch (Exception e){
            logger.info("Sem direito a salário família na rescisão - matrícula {}", numeroMatricula);
            throw new RuntimeException("Erro ao calcular férias proporcionais na rescisão");
        }

        return resultado;
    }

    private BigDecimal calcularSalarioHora(BigDecimal salarioBase) {
        return salarioBase.divide(BigDecimal.valueOf(220), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularValorHoraExtra(BigDecimal salarioHora) {
        // Aqui você pode calcular com os adicionais (50%, 70%, 100%)
        // Por enquanto, retorna o valor base da hora
        return salarioHora;
    }

    private long calcularMesesProporcionais(LocalDate dataAdmissao, LocalDate dataRescisao) {
        long meses = ChronoUnit.MONTHS.between(dataAdmissao, dataRescisao);

        // Verifica se trabalhou 15+ dias no último mês
        LocalDate inicioUltimoMes = dataRescisao.withDayOfMonth(1);
        long diasTrabalhadosUltimoMes = ChronoUnit.DAYS.between(inicioUltimoMes, dataRescisao);

        if (diasTrabalhadosUltimoMes >= 15) {
            meses++; // Conta o mês completo
        }

        return meses;
    }

    private CadastroColaboradores findByMatricula(String matricula){
        Optional<CadastroColaboradores> obj = cadastroColaboradoresRepository.findByNumeroMatricula(matricula);
        return obj.orElseThrow(() ->
                new ObjectNotFoundException("Colaborador não encontrado para matrícula: " + matricula));
    }
}
