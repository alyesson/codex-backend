package br.com.codex.v1.service.rh.impostos;

import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalcularDescontoImpostoRendaService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDescontoImpostoRendaService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularDescontoImpostoRenda() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioBase = folha.getSalarioBase();

            // **Primeiro calcula o INSS**
            BigDecimal descontoInss = calculoBaseService.calcularINSS(salarioBase);

            // **Calcula a base do IRRF (salário - INSS - dependentes - pensão)**
            BigDecimal baseCalculoIrrf = salarioBase.subtract(descontoInss).subtract(BigDecimal.valueOf(folha.getDependentesIrrf())).subtract(folha.getPensaoAlimenticia());

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
            resultado.put("referencia", aliquotaIrrf); // ✅ Alíquota em %
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", descontoIrrf);
        }catch (Exception e){
            logger.error("Erro ao calcular o desconto do imposto de renda para matrícula {}", numeroMatricula, e);
        }

        return resultado;
    }

}
