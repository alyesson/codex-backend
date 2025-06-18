package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.repository.AmbienteNotaFiscalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DfeSchedulerSnu {
    private static final Logger logger = LoggerFactory.getLogger(DfeSchedulerSnu.class);

    @Autowired
    private NotaFiscalService notaFiscalService;

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Value("${dfe.consulta.cnpj}")
    private String cnpjPadrao;

    @Scheduled(fixedRate = 3600000) // A cada hora
    public void consultarDocumentos() {
        try {
            Optional<AmbienteNotaFiscal> ambienteOpt = ambienteNotaFiscalRepository.findById(1L);
            if (ambienteOpt.isEmpty()) {
                throw new IllegalStateException("Configuração de ambiente não encontrada no banco de dados");
            }

            AmbienteNotaFiscal ambiente = ambienteOpt.get();
            String descricaoAmbiente = ambiente.getCodigoAmbiente() == 1 ? "PRODUCAO" : "HOMOLOGACAO";
            notaFiscalService.consultarNSU(cnpjPadrao, descricaoAmbiente);

        } catch (Exception e) {
            logger.error("Erro ao consultar documentos: {}", e.getMessage(), e);
            throw new IllegalStateException("Erro ao consultar documentos", e);
        }
    }
}