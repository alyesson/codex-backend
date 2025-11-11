package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaRescisaoRepository;
import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import br.com.codex.v1.service.rh.decimoterceiro.CalcularDecimoTerceiroComMediaComissoesService;
import br.com.codex.v1.service.rh.decimoterceiro.CalcularInsalubridadePrimeiraParcela13Service;
import br.com.codex.v1.service.rh.decimoterceiro.CalcularMediaAdicionalNoturno13Service;
import br.com.codex.v1.service.rh.decimoterceiro.CalcularPericulosidadePrimeiraParcela13Service;
import br.com.codex.v1.service.rh.ferias.CalcularFeriasProporcionaisComFaltasRescisaoService;
import br.com.codex.v1.service.rh.ferias.CalcularFeriasProporcionaisService;
import br.com.codex.v1.service.rh.rescisao.*;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculoDaFolhaRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaRescisaoService.class);

    @Autowired
    private FolhaRescisaoRepository folhaRescisaoRepository;

    // Services especializados
    @Autowired
    private CalcularSaldoSalarioService calcularSaldoSalarioService;

    @Autowired
    private CalcularAvisoPrevioService calcularAvisoPrevioService;

    @Autowired
    private CalcularMultaFgtsService calcularMultaFGTSService;

    @Autowired
    private CalcularFeriasRescisaoService calcularFeriasRescisaoService;

    @Autowired
    private CalcularDecimoTerceiroRescisaoService calcularDecimoTerceiroRescisaoService;

    @Autowired
    private CalcularImpostosRescisaoService calcularImpostosRescisaoService;

    @Autowired
    private CalcularBeneficiosRescisaoService calcularBeneficiosRescisaoService;

    @Autowired
    private CalcularFeriasProporcionaisRescisaoService calcularFeriasProporcionaisRescisaoService;

    @Autowired
    private CalcularImpostoRendaSobrePlrRescisaoService calcularImpostoRendaSobrePlrRescisaoService;

    @Autowired
    private CalcularInsalubridadePrimeiraParcela13Service calcularInsalubridadePrimeiraParcela13Service;

    @Autowired
    private CalcularPericulosidadePrimeiraParcela13Service calcularPericulosidadePrimeiraParcela13Service;

    @Autowired
    private CalcularMediaHE13ProporcionalService calcularMediaHE13ProporcionalService;

    @Autowired
    private CalcularFaltasAtrasosRescisaoService calcularFaltasAtrasosRescisaoService;

    @Autowired
    private CalcularFeriasProporcionaisService calcularFeriasProporcionaisService;

    @Autowired
    private CalcularFgtsDepositadoService calcularFgtsDepositadoService;

    @Autowired
    private CalcularMediaAdicionalNoturno13Service calcularMediaAdicionalNoturno13Service;

    @Autowired
    private CalcularDecimoTerceiroProporcionalService calcularDecimoTerceiroProporcionalService;

    @Autowired
    private CalcularDecimoTerceiroComAvisoService calcularDecimoTerceiroComAvisoService;

    @Autowired
    private CalcularFeriasProporcionaisComFaltasRescisaoService calcularFeriasProporcionaisComFaltasRescisaoService;

    @Autowired
    private CalcularMediaAdicionalNoturnoRescisaoService calcularMediaAdicionalNoturnoRescisaoService;

    @Autowired
    private CalcularDecimoTerceiroComMediaComissoesService calcularDecimoTerceiroComMediaComissoesService;

    @Autowired
    private CalcularFeriasProporcionaisComAdicionalNoturnoRescisaoService calcularFeriasProporcionaisComAdicionalNoturnoRescisaoService;

    @Setter
    private String numeroMatricula;

    public FolhaRescisao findByNumeroMatricula(String numeroMatricula) {
        Optional<FolhaRescisao> obj = folhaRescisaoRepository.findByNumeroMatricula(numeroMatricula);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação da rescisão não encontrada"));
    }

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            FolhaRescisao rescisao = findByNumeroMatricula(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            BigDecimal salarioPorHora = rescisao.getSalarioHora();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
            LocalDate dataDemissao = rescisao.getDataDeDemissao();
            Integer diasTrabalhadosMes = rescisao.getDiasTrabalhadosNoMes();
            Integer faltasMes = rescisao.getFaltasNoMes();
            Integer numeroDependentes = rescisao.getNumeroDependenteIrrf();
            String tipoSalario = rescisao.getTipoDeSalario();
            String tipoDemissao = rescisao.getTipoDeDemissao();
            BigDecimal participacaoLucros = rescisao.getParticipacaoLucros();

            // Configurar numeroMatricula em todos os services
            configurarServices();

            switch (codigoEvento) {
                case 302 -> { // Saldo de Salário
                    return calcularSaldoSalarioService.calcularSaldoSalario(salarioBase, diasTrabalhadosMes, tipoSalario, salarioPorHora);
                }

                case 303 -> { // Aviso Prévio Trabalhado
                    return calcularAvisoPrevioService.calcularAvisoPrevioTrabalhado(diasTrabalhadosMes);
                }

                case 304 -> { // Aviso Prévio Indenizado
                    return calcularAvisoPrevioService.calcularAvisoPrevioIndenizado(salarioBase, dataAdmissao, dataDemissao, tipoSalario, salarioPorHora);
                }

                case 305 -> { // Multa do FGTS (40%)
                    return calcularMultaFGTSService.calcularMultaFGTS(tipoDemissao);
                }

                case 306 -> { // Férias Proporcionais
                    return calcularFeriasRescisaoService.calcularFeriasProporcionais(salarioBase, dataAdmissao, dataDemissao, faltasMes);
                }

                case 307 -> { // Férias Vencidas
                    return calcularFeriasRescisaoService.calcularFeriasVencidas(salarioBase, faltasMes);
                }

                case 308 ->{ //Média horas extras férias proporcionais
                    calcularFeriasProporcionaisRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularFeriasProporcionaisRescisaoService.calcularFeriasProporcionaisRescisao(dataDemissao, dataAdmissao);
                }

                case 309 ->{
                    calcularFeriasRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularFeriasRescisaoService.calcularInsalubridadeFeriasProporcionais(salarioBase, dataAdmissao, dataDemissao, faltasMes);
                }

                case 310 ->{
                    calcularFeriasRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularFeriasRescisaoService.calcularPericulosidadeFeriasProporcionais(salarioBase, dataAdmissao, dataDemissao, faltasMes, tipoSalario);
                }

                case 311 ->{
                    calcularFeriasProporcionaisComAdicionalNoturnoRescisaoService.setNumeroMatricula(numeroMatricula);
                    calcularFeriasProporcionaisComAdicionalNoturnoRescisaoService.calcularFeriasProporcionaisComAdicionalNoturno(salarioBase, dataAdmissao, dataDemissao, faltasMes, tipoSalario);
                }

                case 312 -> { // Salário Família na Rescisão
                    return calcularBeneficiosRescisaoService.calcularSalarioFamiliaRescisao(salarioBase, numeroDependentes, diasTrabalhadosMes);
                }

                case 313 -> { // INSS Sobre Rescisão
                    return calcularImpostosRescisaoService.calcularINSSRescisao(rescisao);
                }

                case 314 -> { // IRRF Sobre Participação dos lucros na Rescisão
                    calcularImpostoRendaSobrePlrRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularImpostoRendaSobrePlrRescisaoService.calcularImpostoRendaSobrePlrRescisao(participacaoLucros);
                }

                case 317 -> { // IRRF Sobre Rescisão
                    return calcularImpostosRescisaoService.calcularIRRFRescisao(rescisao, numeroDependentes);
                }

                case 324 -> { // 1/3 de Férias
                    return calcularFeriasRescisaoService.calcularUmTercoFerias(salarioBase);
                }

                case 325 -> { // 13º Proporcional
                    return calcularDecimoTerceiroRescisaoService.calcularDecimoTerceiroProporcional(salarioBase, dataAdmissao, dataDemissao, faltasMes);
                }

                case 326 -> { // Insalubridade 13° na rescisão
                    calcularInsalubridadePrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                    return calcularInsalubridadePrimeiraParcela13Service.calcularInsalubridadePrimeiraParcela13();
                }

                case 327 ->{ //Periculosidade 13° salário proporcional rescisão
                    calcularPericulosidadePrimeiraParcela13Service.setNumeroMatricula(numeroMatricula);
                    return  calcularPericulosidadePrimeiraParcela13Service.calcularPericulosidadePrimeiraParcela13();
                }

                case 328 ->{ //Média horas extras 13° salário proporcional rescisão
                    calcularMediaHE13ProporcionalService.setNumeroMatricula(numeroMatricula);
                    return calcularMediaHE13ProporcionalService.calcularMediaHE13Proporcional();
                }

                case 329 ->{ //Adicional noturno 13° salário rescisão
                    calcularMediaAdicionalNoturno13Service.setNumeroMatricula(numeroMatricula);
                    return calcularMediaAdicionalNoturno13Service.calcularMediaAdicionalNoturno13();
                }

                case 330 ->{ //Comissões 13° salário proporcional rescisão
                    calcularDecimoTerceiroComMediaComissoesService.setNumeroMatricula(numeroMatricula);
                    return calcularDecimoTerceiroComMediaComissoesService.calcularDecimoTerceiroComMediaComissoes();
                }

                case 331 ->{ //Multa Artigo 476-A 5° Clt
                    resultado.put("referencia", BigDecimal.ONE);
                    resultado.put("vencimentos", salarioBase); // ou o cálculo específico da multa
                    resultado.put("descontos", BigDecimal.ZERO);
                    return resultado;
                }

                case 332 ->{//Média adicional noturno na rescisão
                    calcularMediaAdicionalNoturnoRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularMediaAdicionalNoturnoRescisaoService.calcularMediaAdicionalNoturnoRescisao();
                }

                case 333 ->{ //décimo terceiro proporcional na rescisão
                    calcularDecimoTerceiroProporcionalService.setNumeroMatricula(numeroMatricula);
                    return calcularDecimoTerceiroProporcionalService.calcularDecimoTerceiroProporcional();
                }

                case 334 ->{// Faltas calculadas nas férias
                    calcularFeriasProporcionaisComFaltasRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularFeriasProporcionaisComFaltasRescisaoService.calcularFeriasProporcionaisComFaltas();
                }

                case 335 ->{ //Faltas e atrasos na rescisão
                    calcularFaltasAtrasosRescisaoService.setNumeroMatricula(numeroMatricula);
                    return  calcularFaltasAtrasosRescisaoService.calcularFaltasAtrasosRescisao();
                }

                case 350 ->{ // 1/12 avos 13 sem aviso prévio
                    calcularDecimoTerceiroProporcionalService.setNumeroMatricula(numeroMatricula);
                    return calcularDecimoTerceiroProporcionalService.calcularDecimoTerceiroProporcional();
                }

                case 351, 353 ->{ //351 calcula 1/12 avos férias sem aviso prévio indenizado e 353 calcula com aviso prévio indenizado
                    calcularFeriasProporcionaisService.setNumeroMatricula(numeroMatricula);
                    return calcularFeriasProporcionaisService.calcularFeriasProporcionais();
                }

                case 352 ->{ // 1/12 avos 13 com aviso prévio
                    calcularDecimoTerceiroComAvisoService.setNumeroMatricula(numeroMatricula);
                    return calcularDecimoTerceiroComAvisoService.calcularDecimoTerceiroComAviso();
                }

                case 402 ->{ //FGTS normal depositado
                    calcularFgtsDepositadoService.setNumeroMatricula(numeroMatricula);
                    return calcularFgtsDepositadoService.calcularFgtsDepositado();
                }

                default -> {
                    logger.warn("Evento de rescisão não implementado: {}", codigoEvento);
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao calcular evento de rescisão {}: {}", codigoEvento, e.getMessage());
            throw new RuntimeException("Erro ao calcular rescisão: " + e.getMessage());
        }

        return resultado;
    }

    private void configurarServices() {
        calcularSaldoSalarioService.setNumeroMatricula(numeroMatricula);
        calcularAvisoPrevioService.setNumeroMatricula(numeroMatricula);
        calcularMultaFGTSService.setNumeroMatricula(numeroMatricula);
        calcularFeriasRescisaoService.setNumeroMatricula(numeroMatricula);
        calcularDecimoTerceiroRescisaoService.setNumeroMatricula(numeroMatricula);
        calcularImpostosRescisaoService.setNumeroMatricula(numeroMatricula);
        calcularBeneficiosRescisaoService.setNumeroMatricula(numeroMatricula);
    }
}