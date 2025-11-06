package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaRescisaoRepository;
import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
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
                    return calcularMultaFGTSService.calcularMultaFGTS(salarioBase, dataAdmissao, dataDemissao, tipoDemissao);
                }
                case 306 -> { // Férias Proporcionais
                    return calcularFeriasRescisaoService.calcularFeriasProporcionais(salarioBase, dataAdmissao, dataDemissao, faltasMes, tipoSalario);
                }
                case 307 -> { // Férias Vencidas
                    return calcularFeriasRescisaoService.calcularFeriasVencidas(salarioBase, faltasMes, tipoSalario);
                }
                case 308 ->{ //Média horas extras férias proporcionais
                    calcularFeriasProporcionaisRescisaoService.setNumeroMatricula(numeroMatricula);
                    return calcularFeriasProporcionaisRescisaoService.calcularFeriasProporcionaisRescisao(dataDemissao, dataAdmissao);
                }
                case 312 -> { // Salário Família na Rescisão
                    return calcularBeneficiosRescisaoService.calcularSalarioFamiliaRescisao(salarioBase, numeroDependentes, diasTrabalhadosMes);
                }
                case 313 -> { // INSS Sobre Rescisão
                    return calcularImpostosRescisaoService.calcularINSSRescisao(rescisao);
                }
                case 314 -> { // IRRF Sobre Rescisão
                    return calcularImpostosRescisaoService.calcularIRRFRescisao(rescisao, numeroDependentes);
                }
                case 324 -> { // 1/3 de Férias
                    return calcularFeriasRescisaoService.calcularUmTercoFerias(salarioBase);
                }
                case 325 -> { // 13º Proporcional
                    return calcularDecimoTerceiroRescisaoService.calcularDecimoTerceiroProporcional(salarioBase, dataAdmissao, dataDemissao, faltasMes);
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