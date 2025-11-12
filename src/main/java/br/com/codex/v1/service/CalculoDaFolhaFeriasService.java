package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaFeriasRepository;
import br.com.codex.v1.domain.rh.FolhaFerias;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import br.com.codex.v1.service.rh.ferias.CalcularAbonoPecuniarioService;
import br.com.codex.v1.service.rh.ferias.CalcularFeriasService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculoDaFolhaFeriasService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaFeriasService.class);

        @Autowired
        private FolhaFeriasRepository folhaFeriasRepository;

        @Autowired
        private CalcularFeriasService calcularFeriasService;

        @Autowired
        private CalcularAbonoPecuniarioService calcularAbonoPecuniarioService;

    @Setter
        private String numeroMatricula;

        public FolhaFerias findByNumeroMatricula(String matricula){
            Optional<FolhaFerias> obj = folhaFeriasRepository.findByNumeroMatricula(matricula);
            return obj.orElseThrow(() -> new ObjectNotFoundException("Informação não encontrada"));
        }

        public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {
            Map<String, BigDecimal> resultado = new HashMap<>();

            try {
                FolhaFerias ferias = findByNumeroMatricula(numeroMatricula);

                BigDecimal salarioBase = ferias.getSalarioBruto();
                BigDecimal salarioPorHora = ferias.getSalarioHora();
                BigDecimal diasDeFerias = BigDecimal.valueOf(ferias.getTotalDiasFerias());
                Integer quantidadeDeFaltas = ferias.getTotalFaltas();
                Integer numeroDependentes = ferias.getNumeroDependentes();

                // Calcular valores base reutilizáveis
                BigDecimal valorDiasFerias = ferias.getValorDiasFerias() != null ?
                        ferias.getValorDiasFerias() :
                        calcularFeriasService.calcularValorFerias(salarioBase, diasDeFerias);

                switch (codigoEvento) {

                    case 140 -> {
                        calcularFeriasService.calcularFeriasGozadas(resultado, new BigDecimal("30"), salarioBase);
                    }

                    case 141 -> {
                        calcularFeriasService.calcularFeriasGozadas(resultado, new BigDecimal("20"), salarioBase);
                    }

                    case 142 -> {
                        calcularFeriasService.calcularFeriasGozadas(resultado, new BigDecimal("15"), salarioBase);
                    }

                    case 143 -> {
                        calcularFeriasService.calcularFeriasGozadas(resultado, new BigDecimal("10"), salarioBase);
                    }

                    case 144 -> {
                        calcularFeriasService.calcularFeriasGozadas(resultado, diasDeFerias, salarioBase);
                    }

                    case 145 -> {
                        BigDecimal umTerco = valorDiasFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", umTerco);
                        resultado.put("vencimentos", umTerco);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    case 146 -> {
                        calcularAbonoPecuniarioService.setNumeroMatricula(numeroMatricula);
                        return calcularAbonoPecuniarioService.calcularAbonoPecuniario();
                    }

                    case 147 -> {
                        BigDecimal valorAbono = calcularFeriasService.calcularValorFerias(salarioBase, new BigDecimal("10"));
                        BigDecimal umTercoAbono = valorAbono.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", umTercoAbono);
                        resultado.put("vencimentos", umTercoAbono);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    case 148 -> {
                        calcularFeriasService.calcularMediaHorasExtras(resultado, 98, salarioPorHora);
                    }

                    case 149 -> {
                        calcularFeriasService.calcularMediaHorasExtras(resultado, 99, salarioPorHora);
                    }

                    case 150 -> {
                        calcularFeriasService.calcularMediaHorasExtras(resultado, 100, salarioPorHora);
                    }

                    case 151 -> {
                        calcularFeriasService.calcularMediaInsalubridade(resultado);
                    }

                    case 152 -> {
                        calcularFeriasService.calcularMediaPericulosidade(resultado, salarioBase);
                    }

                    case 153 -> {
                        calcularFeriasService.calcularMediaComissoes(resultado);
                    }

                    case 154 -> {
                        calcularFeriasService.calcularMediaAdicionalNoturno(resultado);
                    }

                    case 155 -> {
                        BigDecimal diasReduzidos = calcularFeriasService.calcularReducaoFaltasFerias(quantidadeDeFaltas);
                        BigDecimal valorReducao = calcularFeriasService.calcularValorFerias(salarioBase, diasReduzidos);
                        resultado.put("referencia", diasReduzidos);
                        resultado.put("vencimentos", BigDecimal.ZERO);
                        resultado.put("descontos", valorReducao);
                    }

                    case 156 -> {
                        calcularFeriasService.calcularFeriasDobro(resultado, valorDiasFerias, numeroDependentes);
                    }

                    case 157 -> {
                        BigDecimal umTerco = valorDiasFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", umTerco);
                        resultado.put("vencimentos", umTerco);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    case 158 -> {
                        calcularFeriasService.calcularDescontoINSS(resultado, valorDiasFerias);
                    }

                    case 159 -> {
                        calcularFeriasService.calcularDescontoIRRF(resultado, valorDiasFerias, numeroDependentes);
                    }

                    default -> logger.warn("Evento de férias não implementado: {}", codigoEvento);
                }
            } catch (Exception e) {
                logger.error("Erro ao calcular evento de férias {}: {}", codigoEvento, e.getMessage());
                throw new RuntimeException("Erro ao calcular férias: " + e.getMessage());
            }
            return resultado;
        }
}
