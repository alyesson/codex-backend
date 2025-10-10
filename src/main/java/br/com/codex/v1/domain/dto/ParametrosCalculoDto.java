package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
public class ParametrosCalculoDto {

    private Integer codigoEvento;
    private BigDecimal salarioBase;
    private BigDecimal salarioPorHora;
    private BigDecimal salarioMinimo;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private String tipoJornada;
    private BigDecimal valorHoraExtra50;
    private BigDecimal valorHoraExtra70;
    private BigDecimal valorHoraExtra100;
    private BigDecimal percentualInsalubridade;
    private BigDecimal percentualPericulosidade;
    private BigDecimal valorComissao;
    private BigDecimal valeCreche;
    private BigDecimal valorVendasMes;
    private BigDecimal ajudaCusto;
    private BigDecimal valorQuebraCaixa;
    private BigDecimal valorGratificacao;
    private BigDecimal horasPorMes;
    private String numeroMatricula;
    private Integer numeroDependentes;
    private BigDecimal valorValeTransporte;
    private BigDecimal valorValeCreche;
    private BigDecimal valorReferenciaHoraNoturna;
    private BigDecimal valorReferenciaHoraDiurna;
    private BigDecimal valorReferenciaDsrHoraNoturna;
    private BigDecimal media50;
    private BigDecimal media70;
    private BigDecimal media100;
    private BigDecimal mediaValorDsr;
    private BigDecimal mediaAdicionalNoturnoSobreDecimoTerceiro;
    private BigDecimal mediaHorasExtras50SobreDecimoTerceiro;
    private BigDecimal mediaHorasExtras70SobreDecimoTerceiro;
    private BigDecimal mediaHorasExtras100SobreDecimoTerceiro;
    private BigDecimal somaComplemento50;
    private BigDecimal somaComplemento70;
    private BigDecimal somaComplemento100;
    private BigDecimal somaComplementoDsrDiurno;
    private BigDecimal somaComplementoDsrNoturno;
    private BigDecimal calculoMediaDsrSobreDecioTerceiro;
    private BigDecimal calculoMediaDsrNoturnoSobreDecimoTerceiro;
    private BigDecimal mediaHoraExtraSalarioMaternidade50Porcento;
    private BigDecimal mediaHoraExtraSalarioMaternidade70Porcento;
    private BigDecimal mediaHoraExtraSalarioMaternidade100Porcento;
    private BigDecimal mediaValorDsrDiurnoSalarioMaternidade;
    private BigDecimal mediaValorDsrNoturnoSalarioMaternidade;
    private BigDecimal mediaAdicionalNoturnoSalarioMaternidade;
    private BigDecimal mediaComissoesAno;
    private BigDecimal participacaoLucros;
    private BigDecimal abonoSalarial;
    private String descricaoCargo;
}
