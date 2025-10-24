package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
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
import java.util.Optional;

@Service
public class CalculoDaFolhaDescontosService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaDescontosService.class);

    @Autowired
    CalculoBaseService calculoBaseService;

    /*@Autowired
    private FolhaMensalProventosService calculoBaseService;*/

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        System.out.println("🔍 CALCULANDO DESCONTOS - Código: " + codigoEvento + ", Matrícula: " + numeroMatricula);

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

            // Adiantamento 1° Parcela Décimo Terceiro
            case 172 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();

                resultado.put("referencia", valorSalarioBase);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", valorSalarioBase);

                return resultado;
            }

            // Adiantamento 2° Parcela Décimo Terceiro
            case 190 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();
                BigDecimal decimoTerceiroSegundaParcela = valorSalarioBase.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", decimoTerceiroSegundaParcela);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", decimoTerceiroSegundaParcela);

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

            //Desconto Vale Transporte
            case 241 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal valorSalarioBase = folha.getSalarioBase();
                BigDecimal descontoValeTransporte = folha.getSalarioBase()
                        .multiply(new BigDecimal("0.06"))
                        .setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", new BigDecimal(6));
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", descontoValeTransporte);

                return resultado;
            }

            //Desconto Plano Odontológico
            case 242 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal planoOdonto = folha.getValorPlanoOdonto();

                resultado.put("referencia", planoOdonto);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", planoOdonto);

                return resultado;
            }

            //Desconto INSS
            case 244 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                BigDecimal descontoInss = calculoBaseService.calcularINSS(salarioBase);

                resultado.put("referencia", descontoInss);
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", descontoInss);

                return resultado;
            }

            //Desconto Imposto de Renda
            case 245 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();

                // **Primeiro calcula o INSS**
                BigDecimal descontoInss = calculoBaseService.calcularINSS(salarioBase);

                // **Calcula a base do IRRF (salário - INSS - dependentes - pensão)**
                BigDecimal baseCalculoIrrf = salarioBase
                        .subtract(descontoInss)
                        .subtract(BigDecimal.valueOf(folha.getDependentesIrrf()))
                        .subtract(folha.getPensaoAlimenticia());

                // **Calcula o IRRF normalmente**
                BigDecimal descontoIrrf = calculoBaseService.calcularIRRF(baseCalculoIrrf);

                // **AGORA PEGAMOS A ALÍQUOTA AQUI MESMO**
                BigDecimal aliquotaIrrf = BigDecimal.ZERO;

                // Busca a tabela atual do IRRF
                Optional<TabelaImpostoRenda> tabelaIrrfOpt = tabelaImpostoRendaRepository.findTopByOrderById();

                if (tabelaIrrfOpt.isPresent()) {
                    TabelaImpostoRenda tabelaIrrf = tabelaIrrfOpt.get();

                    // Determina a alíquota com base na base de cálculo
                    if (baseCalculoIrrf.compareTo(tabelaIrrf.getFaixaSalario1()) <= 0) {
                        aliquotaIrrf = BigDecimal.ZERO;
                    } else if (baseCalculoIrrf.compareTo(tabelaIrrf.getFaixaSalario2()) <= 0) {
                        aliquotaIrrf = tabelaIrrf.getAliquota1().multiply(BigDecimal.valueOf(100)); // Converte para %
                    } else if (baseCalculoIrrf.compareTo(tabelaIrrf.getFaixaSalario3()) <= 0) {
                        aliquotaIrrf = tabelaIrrf.getAliquota2().multiply(BigDecimal.valueOf(100));
                    } else if (baseCalculoIrrf.compareTo(tabelaIrrf.getFaixaSalario4()) <= 0) {
                        aliquotaIrrf = tabelaIrrf.getAliquota3().multiply(BigDecimal.valueOf(100));
                    } else if (baseCalculoIrrf.compareTo(tabelaIrrf.getFaixaSalario5()) <= 0) {
                        aliquotaIrrf = tabelaIrrf.getAliquota4().multiply(BigDecimal.valueOf(100));
                    } else {
                        aliquotaIrrf = tabelaIrrf.getAliquota5().multiply(BigDecimal.valueOf(100));
                    }
                }

                // **Agora coloca a alíquota na referência**
                resultado.put("referencia", aliquotaIrrf); // ✅ Alíquota em %
                resultado.put("vencimentos", BigDecimal.ZERO);
                resultado.put("descontos", descontoIrrf);

                return resultado;
            }
        }
    } catch (Exception e) {
        logger.error("Erro ao calcular desconto para evento {}: {}", codigoEvento, e.getMessage());
    }

        return resultado;
    }
}
