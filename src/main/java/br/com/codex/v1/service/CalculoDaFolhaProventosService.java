package br.com.codex.v1.service;

import br.com.codex.v1.service.rh.beneficios.*;
import br.com.codex.v1.service.rh.decimoterceiro.*;
import br.com.codex.v1.service.rh.especiais.*;
import br.com.codex.v1.service.rh.horarios.*;
import br.com.codex.v1.service.rh.horasextras.*;
import br.com.codex.v1.service.rh.impostos.CalcularFgts13salarioService;
import br.com.codex.v1.service.rh.impostos.CalcularFgtsSalarioService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalculoDaFolhaProventosService {

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;

    @Autowired
    protected CalcularHorasNormaisDiurnasService calcularHorasNormaisDiurnasService;

    @Autowired
    protected CalcularAdiantamentoSalario40PorcentoService calcularAdiantamentoSalario40PorcentoService;

    @Autowired
    protected CalcularHorasRepousoRemuneradoDiurnoService calcularHorasRepousoRemuneradoDiurnoService;

    @Autowired
    protected CalcularHorasAtestadoMedicoService calcularHorasAtestadoMedicoService;

    @Autowired
    protected CalcularDiasAtestadoMedicoService calcularDiasAtestadoMedicoService;

    @Autowired
    protected CalcularHorasNormaisNoturnasService calcularHorasNormaisNoturnasService;

    @Autowired
    protected CalcularAdicionalNoturnoService calcularAdicionalNoturnoService;

    @Autowired
    protected CalcularProLaboreService calcularProLaboreService;

    @Autowired
    protected CalcularBolsaAuxilioService calcularBolsaAuxilioService;

    @Autowired
    protected  CalcularHorasRepousoRemuneradoNoturnoService calcularHorasRepousoRemuneradoNoturnoService;

    @Autowired
    protected CalcularDsrSobreHoraExtraDiurna50PorcentoService calcularDsrSobreHoraExtraDiurna50PorcentoService;

    @Autowired
    protected CalcularDsrSobreHoraExtraDiurna70PorcentoService calcularDsrSobreHoraExtraDiurna70PorcentoService;

    @Autowired
    protected CalcularDsrSobreHoraExtraDiurna100PorcentoService calcularDsrSobreHoraExtraDiurna100PorcentoService;

    @Autowired
    protected CalcularInsalubridadeService calcularInsalubridadeService;

    @Autowired
    protected CalcularPericulosidadeService calcularPericulosidadeService;

    @Autowired
    protected CalcularComissaoService calcularComissaoService;

    @Autowired
    protected CalcularGratificacaoService calcularGratificacaoService;

    @Autowired
    protected CalcularQuebraCaixaService calcularQuebraCaixaService;

    @Autowired
    protected CalcularHorasExtras50Service calcularHorasExtras50Service;

    @Autowired
    protected CalcularHorasExtras70Service calcularHorasExtras70Service;

    @Autowired
    protected CalcularHorasExtras100Service calcularHorasExtras100Service;

    @Autowired
    protected CalcularSalarioMaternidadeService calcularSalarioMaternidadeService;

    @Autowired
    protected CalcularMediaHoraExtra50SalarioMaternidadeService calcularMediaHoraExtra50SalarioMaternidadeService;

    @Autowired
    protected CalcularMediaHoraExtra70SalarioMaternidadeService calcularMediaHoraExtra70SalarioMaternidadeService;

    @Autowired
    protected CalcularMediaHoraExtra100SalarioMaternidadeService calcularMediaHoraExtra100SalarioMaternidadeService;

    @Autowired
    protected CalcularMediaDsrDiurnoSalarioMaternidadeService calcularMediaDsrDiurnoSalarioMaternidadeService;

    @Autowired
    protected CalcularMediaDsrNoturnoSalarioMaternidadeService calcularMediaDSRNoturnoSalarioMaternidadeService;

    @Autowired
    protected CalcularMediaAdicionalNoturnoSalarioMaternidadeService calcularMediaAdicionalNoturnoSalarioMaternidadeService;

    @Autowired
    protected CalcularSalarioFamiliaService calcularSalarioFamiliaService;

    @Autowired
    protected CalcularAjudaCustoService calcularAjudaCustoService;

    @Autowired
    protected CalcularPrimeiraParcela13Service calcularPrimeiraParcela13Service;

    @Autowired
    protected CalcularMediaHE50PrimeiraParcela13Service calcularMediaHE50PrimeiraParcela13Service;

    @Autowired
    protected CalcularMediaHE70PrimeiraParcela13Service calcularMediaHE70PrimeiraParcela13Service;

    @Autowired
    protected CalcularMediaHE100PrimeiraParcela13Service calcularMediaHE100PrimeiraParcela13Service;

    @Autowired
    protected CalcularDecimoTerceiroCheioAdiantadoService calcularDecimoTerceiroCheioAdiantadoService;

    @Autowired
    protected CalcularMediaDsrNoturnoPrimeiraParcela13Service calcularMediaDsrNoturnoPrimeiraParcela13Service;

    @Autowired
    protected CalcularInsalubridadePrimeiraParcela13Service calcularInsalubridadePrimeiraParcela13Service;

    @Autowired
    protected CalcularPericulosidadePrimeiraParcela13Service calcularPericulosidadePrimeiraParcela13Service;

    @Autowired
    protected CalcularMediaHE50SegundaParcela13Service calcularMediaHE50SegundaParcela13Service;

    @Autowired
    protected CalcularMediaHE70SegundaParcela13Service calcularMediaHE70SegundaParcela13Service;

    @Autowired
    protected CalcularMediaHE100SegundaParcela13Service calcularMediaHE100SegundaParcela13Service;

    @Autowired
    protected CalcularMediaDsrDiurnoSegundaParcela13Service calcularMediaDsrDiurnoSegundaParcela13Service;

    @Autowired
    protected CalcularMediaDsrNoturnoSegundaParcela13Service calcularMediaDsrNoturnoSegundaParcela13Service;

    @Autowired
    protected CalcularInsalubridadeSegundaParcela13Service calcularInsalubridadeSegundaParcela13Service;

    @Autowired
    protected CalcularPericulosidadeSegundaParcela13Service calcularPericulosidadeSegundaParcela13Service;

    @Autowired
    protected CalcularSegundaParcela13Service calcularSegundaParcela13Service;

    @Autowired
    protected CalcularDecimoTerceiroComMediaComissoesService calcularDecimoTerceiroComMediaComissoesService;

    @Autowired
    protected CalcularComplementoMediaHE5013Service calcularComplementoMediaHE5013Service;

    @Autowired
    protected CalcularComplementoMediaHE7013Service calcularComplementoMediaHE7013Service;

    @Autowired
    protected CalcularComplementoMediaHE10013Service calcularComplementoMediaHE10013Service;

    @Autowired
    protected CalcularComplementoDsrDiurno13Service calcularComplementoDsrDiurno13Service;

    @Autowired
    protected CalcularComplementoDsrNoturno13Service calcularComplementoDsrNoturno13Service;

    @Autowired
    protected CalcularMediaAdicionalNoturno13Service calcularMediaAdicionalNoturno13Service;

    @Autowired
    protected CalcularValeTransporteService calcularValeTransporteService;

    @Autowired
    protected CalcularValeCrecheService calcularValeCrecheService;

    @Autowired
    protected CalcularFgtsSalarioService calcularFgtsSalarioService;

    @Autowired
    protected CalcularFgts13salarioService calcularFgts13salarioService;

    @Autowired
    protected CalcularParticipacaoLucrosService calcularParticipacaoLucrosService;

    @Autowired
    protected CalcularAbonoSalarialService calcularAbonoSalarialService;

    @Autowired
    protected CalcularReembolsoCrecheService calcularReembolsoCrecheService;

    @Autowired
    protected CalcularGratificacaoSemestralService calcularGratificacaoSemestralService;

    @Autowired
    protected CalcularReembolsoViagemService calcularReembolsoViagemService;

    @Autowired
    protected CalcularPrimeiraParcelaPlrService calcularPrimeiraParcelaPlrService;

    @Autowired
    protected CalcularSegundaParcelaPlrService calcularSegundaParcelaPlrService;

    @Autowired
    protected CalcularPrimeiraParcelaAbonoSalarialService calcularPrimeiraParcelaAbonoSalarialService;

    @Autowired
    protected CalcularSegundaParcelaAbonoSalarialService calcularSegundaParcelaAbonoSalarialService;

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        switch (codigoEvento) {

            //Calculando Horas Normais Diurnas
               case 1 -> {
                calcularHorasNormaisDiurnasService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasNormaisDiurnasService.calculaHorasNormaisDiurnas();
            }

            //Calculando Adiantamento de Salário (40%)
               case 2 -> {
                   calcularAdiantamentoSalario40PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularAdiantamentoSalario40PorcentoService.calculaAdiantamentoSalarial40Porcento();
            }

            //Calculando Horas Repouso Remunerado Diurno (DSR) no mês
               case 5 -> {
                   calcularHorasRepousoRemuneradoDiurnoService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasRepousoRemuneradoDiurnoService.calcularHorasRepousoRemuneradoDiurno();
            }

            //Horas de Atestado Médico
               case 8 -> {
                   calcularHorasAtestadoMedicoService.setNumeroMatricula(numeroMatricula);
                return calcularHorasAtestadoMedicoService.calcularHorasAtestadoMedico();
            }

            //Dias de atestado médico
               case 9 -> {
                   calcularDiasAtestadoMedicoService.setNumeroMatricula(numeroMatricula);
                return  calcularDiasAtestadoMedicoService.diasAtestadoMedico();
            }

            //Calculando horas normais noturnas
               case 12 -> {
                   calcularHorasNormaisNoturnasService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasNormaisNoturnasService.calcularHorasNormaisNoturnas();
            }

            //Calculando o Adicional Noturno
               case 14 -> {
                   calcularAdicionalNoturnoService.setNumeroMatricula(numeroMatricula);
                return  calcularAdicionalNoturnoService.calcularAdicionalNoturno();
            }

            //Calculando o Pro-Labore
               case 17 -> {
                   calcularProLaboreService.setNumeroMatricula(numeroMatricula);
                return  calcularProLaboreService.calcularProLabore();
            }

            //Calculando Bolsa Auxílio
               case 19 -> {
                   calcularBolsaAuxilioService.setNumeroMatricula(numeroMatricula);
                   calcularBolsaAuxilioService.setTipoSalario(tipoSalario);
                return  calcularBolsaAuxilioService.calcularBolsaAuxilio();
            }

            //Calculando Horas Repouso Remunerado (DSR) Noturno
              case 25 -> {
                  calcularHorasRepousoRemuneradoNoturnoService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasRepousoRemuneradoNoturnoService.calcularHorasRepousoRemuneradoNoturno();
            }

            //Calculando DSR Sobre Hora Extra Diurna 50%
              case 26 -> {
                  calcularDsrSobreHoraExtraDiurna50PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularDsrSobreHoraExtraDiurna50PorcentoService.calcularDsrSobreHoraExtraDiurna50Porcento();
            }

            //Calculando DSR Sobre Hora Extra Diurna 70%
              case 27 -> {
                  calcularDsrSobreHoraExtraDiurna70PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularDsrSobreHoraExtraDiurna70PorcentoService.calcularDsrSobreHoraExtraDiurna70Porcento();
            }

            //Calculando DSR Sobre Hora Extra Diurna 100%
              case 28 -> {
                  calcularDsrSobreHoraExtraDiurna100PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularDsrSobreHoraExtraDiurna100PorcentoService.calcularDsrSobreHoraExtraDiurna100Porcento();
            }

            //Calculando a Insalubridade
              case 46 -> {
                  calcularInsalubridadeService.setNumeroMatricula(numeroMatricula);
                return  calcularInsalubridadeService.calcularInsalubridadeService();
            }

            //Calculando a Periculosidade
              case 47 -> {
                  calcularPericulosidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularPericulosidadeService.calcularPericulosidade();
            }

            //Calculando a Comissão
             case 51 -> {
                 calcularComissaoService.setNumeroMatricula(numeroMatricula);
                return  calcularComissaoService.calcularComissao();
            }

            //Calculando a Gratificação
             case 53 -> {
                 calcularGratificacaoService.setNumeroMatricula(numeroMatricula);
                return  calcularGratificacaoService.calcularGratificacao();
            }

            //Calculando a Quebra Caixa
             case 54 -> {
                 calcularQuebraCaixaService.setNumeroMatricula(numeroMatricula);
                return   calcularQuebraCaixaService.calcularQuebraCaixa();
            }

            //Calculando horas extras 50% feitas no mês.
             case 98 -> {
                 calcularHorasExtras50Service.setNumeroMatricula(numeroMatricula);
                return  calcularHorasExtras50Service.calcularHorasExtras50();
            }

            //Calculando horas extras 70% feitas no mês
             case 99 -> {
                 calcularHorasExtras70Service.setNumeroMatricula(numeroMatricula);
                return  calcularHorasExtras70Service.calcularHorasExtras70();
            }

            //Calculando horas extras 100% feitas no mês.
             case 100 -> {
                 calcularHorasExtras100Service.setNumeroMatricula(numeroMatricula);
                return  calcularHorasExtras100Service.calcularHorasExtras100();
            }

            //Calculando o Salário Maternidade
             case 101 -> {
                 calcularSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularSalarioMaternidadeService.calcularSalarioMaternidade();
            }

            //Calculando Média Horas Extras 50% Sobre Salário Maternidade
             case 102 -> {
                 calcularMediaHoraExtra50SalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHoraExtra50SalarioMaternidadeService.calcularMediaHoraExtra50SalarioMaternidade();
            }

            //Calculando Média Horas Extras70% Sobre Salário Maternidade
            case 103 -> {
                calcularMediaHoraExtra70SalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHoraExtra70SalarioMaternidadeService.calcularMediaHoraExtra70SalarioMaternidade();
            }

            //Calculando Média Horas Extras100% Sobre Salário Maternidade
            case 104 -> {
                calcularMediaHoraExtra100SalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHoraExtra100SalarioMaternidadeService.calcularMediaHoraExtra100SalarioMaternidade();
            }

            // Calculando Média de DSR Diurno Sobre Salário Maternidade
            case 105 -> {
                calcularMediaDsrDiurnoSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaDsrDiurnoSalarioMaternidadeService.calcularMediaDsrDiurnoSalarioMaternidade();
            }

            // Calculando Média de DSR Noturno Sobre Salário Maternidade
            case 106 -> {
                calcularMediaDSRNoturnoSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaDSRNoturnoSalarioMaternidadeService.calcularMediaDSRNoturnoSalarioMaternidade();
            }

            // Média Adicional Noturno Sobre Salário Maternidade
            case 107 -> {
                calcularMediaAdicionalNoturnoSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaAdicionalNoturnoSalarioMaternidadeService.calcularMediaAdicionalNoturnoSalarioMaternidade();
            }

            //Calculando Salário Família
            case 133 -> {
                calcularSalarioFamiliaService.setNumeroMatricula(numeroMatricula);
                return  calcularSalarioFamiliaService.calcularSalarioFamilia();
            }

            //Calculando Ajuda de Custo
            case 130 -> {
                calcularAjudaCustoService.setNumeroMatricula(numeroMatricula);
                return   calcularAjudaCustoService.calcularAjudaCusto();
            }

            //Calculando Primeira Parcela 13°
            case 167 -> {
                calcularPrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                calcularPrimeiraParcela13Service.setTipoSalario(tipoSalario);
                return  calcularPrimeiraParcela13Service.calcularPrimeiraParcela13();
            }

            //Calculando Média de Horas Extras 50% Sobre 1° Parcela do 13°
            case 168 -> {
                calcularMediaHE50PrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE50PrimeiraParcela13Service.calcularMediaHE50PrimeiraParcela13();
            }

            //Calculando Média de Horas Extras 70% Sobre 1° Parcela do 13°
            case 169 -> {
                calcularMediaHE70PrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE70PrimeiraParcela13Service.calcularMediaHE70PrimeiraParcela13();
            }

            //Calculando Média de Horas Extras 100% Sobre 1° Parcela do 13°
            case 170 -> {
                calcularMediaHE100PrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE100PrimeiraParcela13Service.calcularMediaHE100PrimeiraParcela13();
            }

            //Calculando Décimo terceiro cheio Adiantado
            case 171 ->{
                calcularDecimoTerceiroCheioAdiantadoService.setNumeroMatricula(numeroMatricula);
                return  calcularDecimoTerceiroCheioAdiantadoService.calcularDecimoTerceiroCheioAdiantado();
            }

            //Calculando Média de DSR Noturno Sobre 1° Parcela do 13°
            case 177 -> {
                calcularMediaDsrNoturnoPrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaDsrNoturnoPrimeiraParcela13Service.calcularMediaDSRNoturnoPrimeiraParcela13();
            }

            //Calculando Insalubridade sobre Primeira Parcela do 13°
            case 178 -> {
                calcularInsalubridadePrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularInsalubridadePrimeiraParcela13Service.calcularInsalubridadePrimeiraParcela13();
            }

            //Calculando Periculosidade sobre Primeira Parcela do 13°
            case 179 -> {
                calcularPericulosidadePrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularPericulosidadePrimeiraParcela13Service.calcularPericulosidadePrimeiraParcela13();
            }

            //Calculando Média de Horas Extras 50% Sobre 2° Parcela do 13°
            case 182 -> {
                calcularMediaHE50SegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE50SegundaParcela13Service.calcularMediaHE50SegundaParcela13();
            }

            //Calculando Média de Horas Extras 70% Sobre 2° Parcela do 13°
            case 183 -> {
                calcularMediaHE70SegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE70SegundaParcela13Service.calcularMediaHE70SegundaParcela13();
            }

            //Calculando Média de Horas Extras 100% Sobre 2° Parcela do 13°
            case 184 -> {
                calcularMediaHE100SegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE100SegundaParcela13Service.calcularMediaHE100SegundaParcela13();
            }

            //Calculando Média de DSR Diurno Sobre 2° Parcela do 13°
            case 185 -> {
                calcularMediaDsrDiurnoSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularMediaDsrDiurnoSegundaParcela13Service.calcularMediaDsrDiurnoSegundaParcela13();
            }

            //Calculando Média de DSR Noturno Sobre 2° Parcela do 13°
            case 186 ->{
                calcularMediaDsrNoturnoSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularMediaDsrNoturnoSegundaParcela13Service.calcularMediaDsrNoturnoSegundaParcela13();
            }

            //Calculando Insalubridade sobre Segunda Parcela do 13°
            case 187 ->{
                calcularInsalubridadeSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularInsalubridadeSegundaParcela13Service.calcularInsalubridadeSegundaParcela13();
            }

            //Calculando Periculosidade sobre Segunda Parcela do 13°
            case 188 ->{
                calcularPericulosidadeSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularPericulosidadeSegundaParcela13Service.calcularPericulosidadeSegundaParcela13();
            }

            //Calculando a Segunda Parcela do 13°
            case 189 -> {
                calcularSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularSegundaParcela13Service.calcularSegundaParcela13();
            }

            //Calculando 13.º Salário Final Média Comissões
            case 195 -> {
                   calcularDecimoTerceiroComMediaComissoesService.setNumeroMatricula(numeroMatricula);
                return calcularDecimoTerceiroComMediaComissoesService.calcularDecimoTerceiroComMediaComissoes();
            }

            //Calculando o Complemento Média Hora Extra 50% do 13°
            case 201 -> {
                calcularComplementoMediaHE5013Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoMediaHE5013Service.calcularComplementoMediaHE5013();
            }

            //Calculando o Complemento Média Hora Extra 70% do 13°
            case 202 -> {
                calcularComplementoMediaHE7013Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoMediaHE7013Service.calcularComplementoMediaHE7013();
            }

            //Calculando o Complemento Média Hora Extra 100% do 13°
            case 203 -> {
                calcularComplementoMediaHE10013Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoMediaHE10013Service.calcularComplementoMediaHE10013();
            }

            //Calculando Complemento DSR Diurno Sobre o 13°
            case 204 -> {
                calcularComplementoDsrDiurno13Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoDsrDiurno13Service.calcularComplementoDsrDiurno13();
            }

            //Calculando Complemento DSR Noturno Sobre o 13°
            case 205 -> {
                calcularComplementoDsrNoturno13Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoDsrNoturno13Service.calcularComplementoDsrNoturno13();
            }

            //Média Adicional Noturno do 13° Salário
            case 206 -> {
                calcularMediaAdicionalNoturno13Service.setNumeroMatricula(numeroMatricula);
                return calcularMediaAdicionalNoturno13Service.calcularMediaAdicionalNoturno13();
            }

            //Cálculo do Vale Transporte
            case 239 -> {
                calcularValeTransporteService.setNumeroMatricula(numeroMatricula);
                return calcularValeTransporteService.calcularValeTransporte();
            }

            //Cálculo Vale Creche
            case 259 -> {
                calcularValeCrecheService.setNumeroMatricula(numeroMatricula);
                return calcularValeCrecheService.calcularValeCreche();
            }

            //Calculando Fgts Sobre o Salário
            case 402 -> {
                calcularFgtsSalarioService.setNumeroMatricula(numeroMatricula);
              return calcularFgtsSalarioService.calcularFgtsSalario();
            }

            //Calculando Fgts Sobre 13º Salário
            case 403 -> {
                calcularFgts13salarioService.setNumeroMatricula(numeroMatricula);
               return calcularFgts13salarioService.calcularFGTS13salario();
            }

            //Participação Nos Lucros e Resultados
            case 409 -> {
                calcularParticipacaoLucrosService.setNumeroMatricula(numeroMatricula);
              return calcularParticipacaoLucrosService.calcularParticipacaoLucros();
            }

            //Abono Salarial
            case 410 -> {
                calcularAbonoSalarialService.setNumeroMatricula(numeroMatricula);
                return calcularAbonoSalarialService.calcularAbonoSalarial();
            }

            //Cálculo Reembolso Creche
            case 412 -> {
                calcularReembolsoCrecheService.setNumeroMatricula(numeroMatricula);
                return calcularReembolsoCrecheService.calcularReembolsoCreche();
            }

            //Cálculo Gratificação Semestral
            case 414 -> {
                calcularGratificacaoSemestralService.setNumeroMatricula(numeroMatricula);
                return calcularGratificacaoSemestralService.calcularGratificacaoSemestral();
            }

            //Cálculo Reembolso Viagem
            case 416 -> {
                calcularReembolsoViagemService.setNumeroMatricula(numeroMatricula);
                return calcularReembolsoViagemService.calcularReembolsoViagem();
            }

            //1° Parcela Participação Nos Lucros e Resultados
            case 417 -> {
                calcularPrimeiraParcelaPlrService.setNumeroMatricula(numeroMatricula);
                return calcularPrimeiraParcelaPlrService.calcularPrimeiraParcelaPlr();
            }

            //2° Parcela Participação Nos Lucros e Resultados
            case 418 -> {
                calcularSegundaParcelaPlrService.setNumeroMatricula(numeroMatricula);
                return calcularSegundaParcelaPlrService.calcularSegundaParcelaPlr();
            }

            //1° Parcela Abono Salarial
            case 420 -> {
                calcularPrimeiraParcelaAbonoSalarialService.setNumeroMatricula(numeroMatricula);
                return calcularPrimeiraParcelaAbonoSalarialService.calcularPrimeiraParcelaAbonoSalarial();
            }

            //2° Parcela Abono Salarial
            case 421 -> {
                calcularSegundaParcelaAbonoSalarialService.setNumeroMatricula(numeroMatricula);
                return calcularSegundaParcelaAbonoSalarialService.calcularSegundaParcelaAbonoSalarial();
            }
            default -> {
                return resultado;
            }
        }
    }
}