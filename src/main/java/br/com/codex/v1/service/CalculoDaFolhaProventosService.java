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
               case 1 -> {
                calcularHorasNormaisDiurnasService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasNormaisDiurnasService.calculaHorasNormaisDiurnas();
            }

               case 2 -> {
                   calcularAdiantamentoSalario40PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularAdiantamentoSalario40PorcentoService.calculaAdiantamentoSalarial40Porcento();
            }

               case 5 -> {
                   calcularHorasRepousoRemuneradoDiurnoService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasRepousoRemuneradoDiurnoService.calcularHorasRepousoRemuneradoDiurno();
            }

               case 8 -> {
                   calcularHorasAtestadoMedicoService.setNumeroMatricula(numeroMatricula);
                return calcularHorasAtestadoMedicoService.calcularHorasAtestadoMedico();
            }

               case 9 -> {
                   calcularDiasAtestadoMedicoService.setNumeroMatricula(numeroMatricula);
                return  calcularDiasAtestadoMedicoService.diasAtestadoMedico();
            }

               case 12 -> {
                   calcularHorasNormaisNoturnasService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasNormaisNoturnasService.calcularHorasNormaisNoturnas();
            }

               case 14 -> {
                   calcularAdicionalNoturnoService.setNumeroMatricula(numeroMatricula);
                return  calcularAdicionalNoturnoService.calcularAdicionalNoturno();
            }

               case 17 -> {
                   calcularProLaboreService.setNumeroMatricula(numeroMatricula);
                return  calcularProLaboreService.calcularProLabore();
            }

               case 19 -> {
                   calcularBolsaAuxilioService.setNumeroMatricula(numeroMatricula); calcularBolsaAuxilioService.setTipoSalario(tipoSalario);
                return  calcularBolsaAuxilioService.calcularBolsaAuxilio();
            }

              case 25 -> {
                  calcularHorasRepousoRemuneradoNoturnoService.setNumeroMatricula(numeroMatricula);
                return  calcularHorasRepousoRemuneradoNoturnoService.calcularHorasRepousoRemuneradoNoturno();
            }

              case 26 -> {
                  calcularDsrSobreHoraExtraDiurna50PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularDsrSobreHoraExtraDiurna50PorcentoService.calcularDsrSobreHoraExtraDiurna50Porcento();
            }

              case 27 -> {
                  calcularDsrSobreHoraExtraDiurna70PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularDsrSobreHoraExtraDiurna70PorcentoService.calcularDsrSobreHoraExtraDiurna70Porcento();
            }

              case 28 -> {
                  calcularDsrSobreHoraExtraDiurna100PorcentoService.setNumeroMatricula(numeroMatricula);
                return  calcularDsrSobreHoraExtraDiurna100PorcentoService.calcularDsrSobreHoraExtraDiurna100Porcento();
            }

              case 46 -> {
                  calcularInsalubridadeService.setNumeroMatricula(numeroMatricula);
                return  calcularInsalubridadeService.calcularInsalubridadeService();
            }

              case 47 -> {
                  calcularPericulosidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularPericulosidadeService.calcularPericulosidade();
            }

             case 51 -> {
                 calcularComissaoService.setNumeroMatricula(numeroMatricula);
                return  calcularComissaoService.calcularComissao();
            }

             case 53 -> {
                 calcularGratificacaoService.setNumeroMatricula(numeroMatricula);
                return  calcularGratificacaoService.calcularGratificacao();
            }

             case 54 -> {
                 calcularQuebraCaixaService.setNumeroMatricula(numeroMatricula);
                return   calcularQuebraCaixaService.calcularQuebraCaixa();
            }

            case 56 -> {
                calcularQuebraCaixaService.setNumeroMatricula(numeroMatricula);
                return   calcularQuebraCaixaService.calcularQuebraCaixa();
            }

             case 98 -> {
                 calcularHorasExtras50Service.setNumeroMatricula(numeroMatricula);
                return  calcularHorasExtras50Service.calcularHorasExtras50();
            }

             case 99 -> {
                 calcularHorasExtras70Service.setNumeroMatricula(numeroMatricula);
                return  calcularHorasExtras70Service.calcularHorasExtras70();
            }

             case 100 -> {
                 calcularHorasExtras100Service.setNumeroMatricula(numeroMatricula);
                return  calcularHorasExtras100Service.calcularHorasExtras100();
            }

             case 101 -> {
                 calcularSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularSalarioMaternidadeService.calcularSalarioMaternidade();
            }

             case 102 -> {
                 calcularMediaHoraExtra50SalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHoraExtra50SalarioMaternidadeService.calcularMediaHoraExtra50SalarioMaternidade();
            }

            case 103 -> {
                calcularMediaHoraExtra70SalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHoraExtra70SalarioMaternidadeService.calcularMediaHoraExtra70SalarioMaternidade();
            }

            case 104 -> {
                calcularMediaHoraExtra100SalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHoraExtra100SalarioMaternidadeService.calcularMediaHoraExtra100SalarioMaternidade();
            }

            case 105 -> {
                calcularMediaDsrDiurnoSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaDsrDiurnoSalarioMaternidadeService.calcularMediaDsrDiurnoSalarioMaternidade();
            }

            case 106 -> {
                calcularMediaDSRNoturnoSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaDSRNoturnoSalarioMaternidadeService.calcularMediaDSRNoturnoSalarioMaternidade();
            }

            case 107 -> {
                calcularMediaAdicionalNoturnoSalarioMaternidadeService.setNumeroMatricula(numeroMatricula);
                return  calcularMediaAdicionalNoturnoSalarioMaternidadeService.calcularMediaAdicionalNoturnoSalarioMaternidade();
            }

            case 133 -> {
                calcularSalarioFamiliaService.setNumeroMatricula(numeroMatricula);
                return  calcularSalarioFamiliaService.calcularSalarioFamilia();
            }

            case 130 -> {
                calcularAjudaCustoService.setNumeroMatricula(numeroMatricula);
                calcularAjudaCustoService.setTipoSalario(tipoSalario);
                return   calcularAjudaCustoService.calcularAjudaCusto();
            }

            case 167 -> {
                calcularPrimeiraParcela13Service.setNumeroMatricula(numeroMatricula); calcularPrimeiraParcela13Service.setTipoSalario(tipoSalario);
                return  calcularPrimeiraParcela13Service.calcularPrimeiraParcela13();
            }

            case 168 -> {
                calcularMediaHE50PrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE50PrimeiraParcela13Service.calcularMediaHE50PrimeiraParcela13();
            }

            case 169 -> {
                calcularMediaHE70PrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE70PrimeiraParcela13Service.calcularMediaHE70PrimeiraParcela13();
            }

            case 170 -> {
                calcularMediaHE100PrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE100PrimeiraParcela13Service.calcularMediaHE100PrimeiraParcela13();
            }

            case 171 ->{
                calcularDecimoTerceiroCheioAdiantadoService.setNumeroMatricula(numeroMatricula);
                return  calcularDecimoTerceiroCheioAdiantadoService.calcularDecimoTerceiroCheioAdiantado();
            }

            case 177 -> {
                calcularMediaDsrNoturnoPrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaDsrNoturnoPrimeiraParcela13Service.calcularMediaDSRNoturnoPrimeiraParcela13();
            }

            case 178 -> {
                calcularInsalubridadePrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularInsalubridadePrimeiraParcela13Service.calcularInsalubridadePrimeiraParcela13();
            }

            case 179 -> {
                calcularPericulosidadePrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularPericulosidadePrimeiraParcela13Service.calcularPericulosidadePrimeiraParcela13();
            }

            case 182 -> {
                calcularMediaHE50SegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE50SegundaParcela13Service.calcularMediaHE50SegundaParcela13();
            }

            case 183 -> {
                calcularMediaHE70SegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE70SegundaParcela13Service.calcularMediaHE70SegundaParcela13();
            }

            case 184 -> {
                calcularMediaHE100SegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return  calcularMediaHE100SegundaParcela13Service.calcularMediaHE100SegundaParcela13();
            }

            case 185 -> {
                calcularMediaDsrDiurnoSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularMediaDsrDiurnoSegundaParcela13Service.calcularMediaDsrDiurnoSegundaParcela13();
            }

            case 186 ->{
                calcularMediaDsrNoturnoSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularMediaDsrNoturnoSegundaParcela13Service.calcularMediaDsrNoturnoSegundaParcela13();
            }

            case 187 ->{
                calcularInsalubridadeSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularInsalubridadeSegundaParcela13Service.calcularInsalubridadeSegundaParcela13();
            }

            case 188 ->{
                calcularPericulosidadeSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularPericulosidadeSegundaParcela13Service.calcularPericulosidadeSegundaParcela13();
            }

            case 189 -> {
                calcularSegundaParcela13Service.setNumeroMatricula(numeroMatricula);
                return calcularSegundaParcela13Service.calcularSegundaParcela13();
            }

            case 195 -> {
                   calcularDecimoTerceiroComMediaComissoesService.setNumeroMatricula(numeroMatricula);
                return calcularDecimoTerceiroComMediaComissoesService.calcularDecimoTerceiroComMediaComissoes();
            }

            case 201 -> {
                calcularComplementoMediaHE5013Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoMediaHE5013Service.calcularComplementoMediaHE5013();
            }

            case 202 -> {
                calcularComplementoMediaHE7013Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoMediaHE7013Service.calcularComplementoMediaHE7013();
            }

            case 203 -> {
                calcularComplementoMediaHE10013Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoMediaHE10013Service.calcularComplementoMediaHE10013();
            }

            case 204 -> {
                calcularComplementoDsrDiurno13Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoDsrDiurno13Service.calcularComplementoDsrDiurno13();
            }

            case 205 -> {
                calcularComplementoDsrNoturno13Service.setNumeroMatricula(numeroMatricula);
                return calcularComplementoDsrNoturno13Service.calcularComplementoDsrNoturno13();
            }

            case 206 -> {
                calcularMediaAdicionalNoturno13Service.setNumeroMatricula(numeroMatricula);
                return calcularMediaAdicionalNoturno13Service.calcularMediaAdicionalNoturno13();
            }

            case 239 -> {
                calcularValeTransporteService.setNumeroMatricula(numeroMatricula);
                return calcularValeTransporteService.calcularValeTransporte();
            }

            case 259 -> {
                calcularValeCrecheService.setNumeroMatricula(numeroMatricula);
                return calcularValeCrecheService.calcularValeCreche();
            }

            case 402 -> {
                calcularFgtsSalarioService.setNumeroMatricula(numeroMatricula);
              return calcularFgtsSalarioService.calcularFgtsSalario();
            }

            case 403 -> {
                calcularFgts13salarioService.setNumeroMatricula(numeroMatricula);
               return calcularFgts13salarioService.calcularFGTS13salario();
            }

            case 409 -> {
                calcularParticipacaoLucrosService.setNumeroMatricula(numeroMatricula);
              return calcularParticipacaoLucrosService.calcularParticipacaoLucros();
            }

            case 410 -> {
                calcularAbonoSalarialService.setNumeroMatricula(numeroMatricula);
                return calcularAbonoSalarialService.calcularAbonoSalarial();
            }

            case 412 -> {
                calcularReembolsoCrecheService.setNumeroMatricula(numeroMatricula);
                return calcularReembolsoCrecheService.calcularReembolsoCreche();
            }

            case 414 -> {
                calcularGratificacaoSemestralService.setNumeroMatricula(numeroMatricula);
                return calcularGratificacaoSemestralService.calcularGratificacaoSemestral();
            }

            case 416 -> {
                calcularReembolsoViagemService.setNumeroMatricula(numeroMatricula);
                return calcularReembolsoViagemService.calcularReembolsoViagem();
            }

            case 417 -> {
                calcularPrimeiraParcelaPlrService.setNumeroMatricula(numeroMatricula);
                return calcularPrimeiraParcelaPlrService.calcularPrimeiraParcelaPlr();
            }

            case 418 -> {
                calcularSegundaParcelaPlrService.setNumeroMatricula(numeroMatricula);
                return calcularSegundaParcelaPlrService.calcularSegundaParcelaPlr();
            }

            case 420 -> {
                calcularPrimeiraParcelaAbonoSalarialService.setNumeroMatricula(numeroMatricula);
                return calcularPrimeiraParcelaAbonoSalarialService.calcularPrimeiraParcelaAbonoSalarial();
            }

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