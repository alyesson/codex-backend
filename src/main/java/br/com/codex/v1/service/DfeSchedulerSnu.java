package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.repository.AmbienteNotaFiscalRepository;
import br.com.codex.v1.domain.repository.ConfiguracaoCertificadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DfeSchedulerSnu {
    private static final Logger logger = LoggerFactory.getLogger(DfeSchedulerSnu.class);

    private final NotaFiscalService notaFiscalService;
    private final AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;
    private final ConfiguracaoCertificadoRepository certificadoRepository;

    @Autowired
    public DfeSchedulerSnu(NotaFiscalService notaFiscalService, AmbienteNotaFiscalRepository ambienteNotaFiscalRepository, ConfiguracaoCertificadoRepository certificadoRepository) {
        this.notaFiscalService = notaFiscalService;
        this.ambienteNotaFiscalRepository = ambienteNotaFiscalRepository;
        this.certificadoRepository = certificadoRepository;
    }

    @Scheduled(fixedRate = 3600000) // A cada hora
    public void consultarDocumentos() {
        try {
            // 1. Obter configuração do ambiente
            Optional<AmbienteNotaFiscal> ambienteOpt = ambienteNotaFiscalRepository.findById(1L);
            String descricaoAmbiente = ambienteOpt.map(amb ->
                    amb.getCodigoAmbiente() == 1 ? "PRODUCAO" : "HOMOLOGACAO"
            ).orElseThrow(() ->
                    new IllegalStateException("Configuração de ambiente não encontrada")
            );

            // 2. Buscar todos os CNPJs cadastrados (ou apenas os ativos)
            List<ConfiguracaoCertificado> certificados = certificadoRepository.findAll();

            if (certificados.isEmpty()) {
                logger.warn("Nenhum certificado/emissor cadastrado para consulta DFe");
                return;
            }

            // 3. Processar consulta para cada emissor
            for (ConfiguracaoCertificado cert : certificados) {
                try {
                    String cnpjEmissor = cert.getCnpj();
                    logger.info("Consultando DFe para CNPJ: {}", cnpjEmissor);
                    notaFiscalService.consultarNSU(cnpjEmissor, descricaoAmbiente);
                } catch (Exception e) {
                    logger.error("Falha ao consultar DFe para CNPJ: " + cert.getCnpj(), e);
                }
            }

        } catch (Exception e) {
            logger.error("Erro geral no scheduler de consulta DFe", e);
        }
    }
}