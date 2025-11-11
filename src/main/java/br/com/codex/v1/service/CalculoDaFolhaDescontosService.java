package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.decimoterceiro.CalcularIrrfDecimoTerceiroService;
import br.com.codex.v1.service.rh.descontos.CalcularConvenioAssistenciaOdontologicaService;
import br.com.codex.v1.service.rh.descontos.CalcularDescontoInssService;
import br.com.codex.v1.service.rh.descontos.CalcularDescontoValeTransporteService;
import br.com.codex.v1.service.rh.impostos.CalcularDescontoImpostoRendaService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculoDaFolhaDescontosService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaDescontosService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Autowired
    private CalcularIrrfDecimoTerceiroService calcularIrrfDecimoTerceiroService;

    @Autowired
    private CalcularDescontoImpostoRendaService calcularDescontoImpostoRendaService;

    @Autowired
    private CalcularConvenioAssistenciaOdontologicaService calcularConvenioAssistenciaOdontologicaService;

    @Autowired
    private CalcularDescontoValeTransporteService calcularDescontoValeTransporteService;

    @Autowired
    private CalcularDescontoInssService calcularDescontoInssService;

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

    try{
        switch (codigoEvento) {

            // Desconto Dias de Faltas
            case 6 ->{

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalTime horaEntrada = folha.getHoraEntrada();
                LocalTime horaSaida = folha.getHoraSaida();
                BigDecimal faltaMes = BigDecimal.valueOf(folha.getFaltasMes());
                BigDecimal valorSalarHora = folha.getSalarioHora();
                BigDecimal valorFalta = calculoBaseService.calcularDescontoPorHoras(valorSalarHora, faltaMes, horaEntrada, horaSaida);

                resultado.put("referencia", faltaMes);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorFalta);

                return resultado;
            }

            // Desconto DSR (Descanso Semanal Remunerado)
            case 7 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalTime horaEntrada = folha.getHoraEntrada();
                LocalTime horaSaida = folha.getHoraSaida();
                BigDecimal faltasDsr = BigDecimal.valueOf(folha.getFaltasDsr());
                BigDecimal valorSalarHora = folha.getSalarioHora();

                BigDecimal valorFalta = calculoBaseService.calcularDescontoPorHoras(valorSalarHora, faltasDsr, horaEntrada, horaSaida);

                resultado.put("referencia", faltasDsr);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorFalta);

                return resultado;
            }

            // Desconto por Atraso
            case 15 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalTime horaEntrada = folha.getHoraEntrada();
                LocalTime horaSaida = folha.getHoraSaida();
                BigDecimal diasDeFaltaDha = folha.getFaltasHorasMes();
                BigDecimal valorSalarHora = folha.getSalarioHora();

                BigDecimal valorFaltaDha = calculoBaseService.calcularDescontoPorHoras(valorSalarHora, diasDeFaltaDha, horaEntrada, horaSaida);

                resultado.put("referencia", diasDeFaltaDha);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorFaltaDha);

                return resultado;
            }

            // Desconto por Faltas em Feriado
            case 18 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalTime horaEntrada = folha.getHoraEntrada();
                LocalTime horaSaida = folha.getHoraSaida();
                BigDecimal faltaFeriados = BigDecimal.valueOf(folha.getFaltasFeriados());
                BigDecimal valorSalarHora = folha.getSalarioHora();

                BigDecimal valorFaltaF = calculoBaseService.calcularDescontoPorHoras(valorSalarHora, faltaFeriados, horaEntrada, horaSaida);

                resultado.put("referencia", faltaFeriados);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorFaltaF);

                return resultado;
            }

            // Desconto por Suspensão Disciplinar
            case 23 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalTime horaEntrada = folha.getHoraEntrada();
                LocalTime horaSaida = folha.getHoraSaida();
                BigDecimal faltaSupensao = BigDecimal.valueOf(folha.getFaltasMes());
                BigDecimal valorSalarHora = folha.getSalarioHora();

                BigDecimal valorFaltaSuspensao = calculoBaseService.calcularDescontoPorHoras(valorSalarHora, faltaSupensao, horaEntrada, horaSaida);

                resultado.put("referencia", faltaSupensao);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorFaltaSuspensao);

                return resultado;
            }

            // Desconto Adiantamento de Salário
            case 44 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();
                BigDecimal descontoAdiantamentoSalarial = valorSalarioBase.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", descontoAdiantamentoSalarial);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", descontoAdiantamentoSalarial);

                return resultado;
            }

            // Desconto Salário Maternidade
            case 131 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valorSalarioBase = folha.getSalarioBase();

                resultado.put("referencia", valorSalarioBase);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorSalarioBase);

                return resultado;
            }

            // Empréstimo Consignado
            case 229 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal emprestimoConsignado = folha.getEmprestimoConsignado();

                resultado.put("referencia", emprestimoConsignado);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", emprestimoConsignado);

                return resultado;
            }

            //Desconto Vale Alimentação
            case 231 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valoAlimentacao = folha.getValorValAlimentacao();

                resultado.put("referencia", valoAlimentacao);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valoAlimentacao);

                return resultado;
            }

            //Desconto Vale Farmácia
            case 232 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valeFarmacia = folha.getValeFarmacia();

                resultado.put("referencia", valeFarmacia);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valeFarmacia);

                return resultado;
            }

            //Desconto Vale Refeição
            case 233 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valeRefeicao = folha.getValorValeRefeicao();

                resultado.put("referencia", valeRefeicao);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valeRefeicao);

                return resultado;
            }

            //Desconto Plano Médico
            case 235 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal planoMedico = folha.getValorPlanoMedico();

                resultado.put("referencia", planoMedico);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", planoMedico);

                return resultado;
            }

            //Desconto Plano Seguro de Vida
            case 236 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal seguroVida = folha.getSeguroVida();

                resultado.put("referencia", seguroVida);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", seguroVida);

                return resultado;
            }

            case 241 -> {
                calcularDescontoValeTransporteService.setNumeroMatricula(numeroMatricula);
                return calcularDescontoValeTransporteService.calcularDescontoValeTransporte();
            }

            case 242, 256 -> {
                calcularConvenioAssistenciaOdontologicaService.setNumeroMatricula(numeroMatricula);
                return calcularConvenioAssistenciaOdontologicaService.calcularConvenioAssistenciaOdontologica();
            }

            case 243 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal planoMedico = folha.getValorPlanoMedico();

                resultado.put("referencia", planoMedico);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", planoMedico);

                return resultado;
            }

            case 244 ->{
                calcularDescontoInssService.setNumeroMatricula(numeroMatricula);
                return calcularDescontoInssService.calcularDescontoInss();
            }

            case 245 ->{
                LocalDate dataCalculo = LocalDate.now();
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                return calculoBaseService.calcularINSSDecimoTerceiro(folha.getDataAdmissao(), dataCalculo, salarioBase);
            }

            case 246 -> {
                calcularIrrfDecimoTerceiroService.setNumeroMatricula(numeroMatricula);
                return calcularDescontoImpostoRendaService.calcularDescontoImpostoRenda();
            }

            case 247 -> {
                calcularIrrfDecimoTerceiroService.setNumeroMatricula(numeroMatricula);
                return calcularIrrfDecimoTerceiroService.calcularIrrfDecimoTerceiro();
            }

            case 248 ->{
              FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
              BigDecimal planoMedico = folha.getPensaoAlimenticia();

              resultado.put("referencia", planoMedico);
              resultado.put("vencimentos", BigDecimal.ZERO);
              resultado.put("descontos", planoMedico);

              return resultado;
            }

        }
    } catch (Exception e) {
        logger.error("Erro ao calcular desconto para evento {}: {}", codigoEvento, e.getMessage());
    }
        return resultado;
    }
}
