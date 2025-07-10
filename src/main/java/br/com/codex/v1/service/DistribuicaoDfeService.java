package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.contabilidade.ControleNsu;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.AmbienteNotaFiscalRepository;
import br.com.codex.v1.domain.repository.ConfiguracaoCertificadoRepository;
import br.com.codex.v1.domain.repository.ControleNsuRepository;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.ConsultaDFeEnum;
import br.com.swconsultoria.nfe.dom.enuns.PessoaEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static br.com.swconsultoria.nfe.dom.enuns.PessoaEnum.JURIDICA;

@Service
public class DistribuicaoDfeService {

    private static final Logger logger = LoggerFactory.getLogger(DistribuicaoDfeService.class);

    @Autowired
    private ControleNsuRepository controleNsuRepository;

    @Autowired
    private NotaFiscalService notaFiscalService;

    @Autowired
    private ImportarXmlService importarXmlService;

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Autowired
    private ConfiguracaoCertificadoRepository certificadoRepository;

    @Scheduled(fixedRate = 3600000) // A cada hora
    public void consultarDocumentos() {
        try {
            Optional<AmbienteNotaFiscal> ambienteOpt = ambienteNotaFiscalRepository.findById(1L);
            String ambiente = ambienteOpt.map(amb -> amb.getCodigoAmbiente() == 1 ? "PRODUCAO" : "HOMOLOGACAO")
                    .orElseThrow(() -> new IllegalStateException("Ambiente não configurado"));

            List<ConfiguracaoCertificado> certificados = certificadoRepository.findAll();

            for (ConfiguracaoCertificado cert : certificados) {
                try {
                    String cnpj = cert.getCnpj();
                    logger.info("Processando DFe para CNPJ: {}", cnpj);

                    // 1. Busca os documentos na SEFAZ
                    List<byte[]> documentos = buscarDocumentosPendentes(cnpj, ambiente);

                    // 2. Processa cada XML
                    for (byte[] docZip : documentos) {
                        String xml = decompressGzip(docZip); // Métudo do DistribuicaoDfeService
                        importarXmlService.obterXmlCompletoAutomatico(xml); // Novo métudo
                    }

                } catch (Exception e) {
                    logger.error("Falha no CNPJ " + cert.getCnpj(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Erro no scheduler", e);
        }
    }

    private String decompressGzip(byte[] compressed) throws IOException {
        return new String(new GZIPInputStream(new ByteArrayInputStream(compressed)).readAllBytes());
    }

    /**
     * Busca documentos pendentes na SEFAZ e retorna os XMLs compactados para processamento
     * @return Lista de documentos no formato byte[] (GZIP)
     */
    public List<byte[]> buscarDocumentosPendentes(String cnpj, String ambiente) throws Exception {
        // Validação do CNPJ
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        if (cnpjLimpo.length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        logger.info("Buscando documentos pendentes para CNPJ: {}, ambiente: {}", cnpjLimpo, ambiente);

        // Configuração e consulta do último NSU
        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpjLimpo);
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracao(dto);

        Optional<ControleNsu> controleNsuOpt = controleNsuRepository.findByCnpjAndAmbiente(cnpjLimpo, ambiente);
        String ultimoNsu = controleNsuOpt.map(c -> String.valueOf(c.getUltimoNsu())).orElse("0");

        // Consulta à SEFAZ
        RetDistDFeInt retorno = Nfe.distribuicaoDfe(config, PessoaEnum.JURIDICA, cnpjLimpo, ConsultaDFeEnum.NSU, ultimoNsu);

        // Atualiza o NSU no banco
        if (controleNsuOpt.isPresent()) {
            ControleNsu controleNsu = controleNsuOpt.get();
            controleNsu.setUltimoNsu(Long.parseLong(retorno.getMaxNSU()));
            controleNsu.setDataUltimaConsulta(LocalDateTime.now());
            controleNsuRepository.save(controleNsu);
        } else {
            controleNsuRepository.save(
                    new ControleNsu(ambiente, LocalDateTime.now(), Long.parseLong(retorno.getMaxNSU()), cnpjLimpo, null));
        }

        // Retorna os documentos compactados (GZIP)
        if (retorno.getLoteDistDFeInt() == null || retorno.getLoteDistDFeInt().getDocZip().isEmpty()) {
            logger.info("Nenhum novo documento encontrado para CNPJ {}", cnpjLimpo);
            return Collections.emptyList();
        }

        return retorno.getLoteDistDFeInt().getDocZip().stream()
                .map(RetDistDFeInt.LoteDistDFeInt.DocZip::getValue)
                .collect(Collectors.toList());
    }
}