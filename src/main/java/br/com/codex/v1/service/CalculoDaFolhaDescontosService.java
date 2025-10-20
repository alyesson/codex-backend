package br.com.codex.v1.service;

import br.com.codex.v1.domain.rh.FolhaMensal;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalculoDaFolhaDescontosService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaDescontosService.class);

    @Autowired
    CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Autowired
    private TabelaDeducaoInssService tabelaDeducaoInssService;

    @Autowired
    private FolhaMensalProventosService folhaMensalProventosService;

    @Setter
    String numeroMatricula;
    BigDecimal valorReferenteHoraDiurna;

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        switch (codigoEvento) {

            // Desconto Dias de Faltas
            case 6 ->{

                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

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
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

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

            case 15 ->{
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

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
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

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
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

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
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();
                BigDecimal descontoAdiantamentoSalarial = valorSalarioBase.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", descontoAdiantamentoSalarial);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", descontoAdiantamentoSalarial);

                return resultado;
            }

            // Desconto Salário Maternidade
            case 131 ->{
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();

                resultado.put("referencia", valorSalarioBase);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorSalarioBase);

                return resultado;
            }

            // Adiantamento 1° Parcela Décimo Terceiro
            case 172 ->{
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();

                resultado.put("referencia", valorSalarioBase);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorSalarioBase);

                return resultado;
            }

            // Adiantamento 2° Parcela Décimo Terceiro
            case 190 ->{
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();
                BigDecimal decimoTerceiroSegundaParcela = valorSalarioBase.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", decimoTerceiroSegundaParcela);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", decimoTerceiroSegundaParcela);

                return resultado;
            }

            // Empréstimo Consignado
            case 229 ->{
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal emprestimoConsignado = folha.getEmprestimoConsignado();

                resultado.put("referencia", emprestimoConsignado);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", emprestimoConsignado);

                return resultado;
            }

            //Desconto Vale Alimentação
            case 231 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valoAlimentacao = folha.getValorValAlimentacao();

                resultado.put("referencia", valoAlimentacao);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valoAlimentacao);

                return resultado;
            }

            //Desconto Vale Farmácia
            case 232 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valeFarmacia = folha.getValeFarmacia();

                resultado.put("referencia", valeFarmacia);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valeFarmacia);

                return resultado;
            }

            //Desconto Vale Refeição
            case 233 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valeRefeicao = folha.getValorValeRefeicao();

                resultado.put("referencia", valeRefeicao);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valeRefeicao);

                return resultado;
            }

            //Desconto Plano Médico
            case 235 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal planoMedico = folha.getValorPlanoMedico();

                resultado.put("referencia", planoMedico);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", planoMedico);

                return resultado;
            }

            //Desconto Plano Seguro de Vida
            case 236 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal seguroVida = folha.getSeguroVida();

                resultado.put("referencia", seguroVida);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", seguroVida);

                return resultado;
            }

            //Desconto Vale Transporte
            case 241 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal percentualDescontoValeTransporte = new BigDecimal("0.06");
                BigDecimal valorSalarioBase = folha.getSalarioBase();
                BigDecimal descontaValeTransporte = valorSalarioBase.multiply(percentualDescontoValeTransporte).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", percentualDescontoValeTransporte);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", descontaValeTransporte);

                return resultado;
            }

            //Desconto Plano Odontológico
            case 242 -> {
                FolhaMensal folha = folhaMensalProventosService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal planoOdonto = folha.getValorPlanoOdonto();

                resultado.put("referencia", planoOdonto);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", planoOdonto);

                return resultado;
            }
        }

        return resultado;
    }
}
