package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.ControleNsu;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.ControleNsuRepository;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

@Service
public class DistribuicaoDfeService {

    private static final Logger logger = LoggerFactory.getLogger(DistribuicaoDfeService.class);

    @Autowired
    private ControleNsuRepository controleNsuRepository;

    @Autowired
    private NotaFiscalService notaFiscalService;

    @Transactional
    public void consultarDocumentos(String cnpj, String ambiente) throws NfeException {
        logger.info("Consultando DF-e para CNPJ: {}, ambiente: {}", cnpj, ambiente);
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracao(new NotaFiscalDto().setDocumentoEmitente(cnpj));
        Optional<ControleNsu> controleNsuOpt = controleNsuRepository.findByCnpjAndAmbiente(cnpj, ambiente);
        ControleNsu controleNsu = controleNsuOpt.orElseGet(() -> new ControleNsu(cnpj, ambiente, 0L, LocalDateTime.now()));

        RetDistDFeInt retorno = Nfe.distribuicaoDfe(config, cnpj, String.valueOf(controleNsu.getUltimoNsu()));
        Long maxNSU = Long.parseLong(retorno.getMaxNSU());

        if (retorno.getLoteDistDFeInt() != null) {
            for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retorno.getLoteDistDFeInt().getDocZip()) {
                try {
                    String xml = decompressGzip(doc.getValue());
                    logger.info("Documento NSU {} processado: {}", doc.getNSU(), xml.substring(0, Math.min(xml.length(), 100)));
                    // Salvar ou processar XML
                } catch (IOException e) {
                    logger.error("Erro ao descomprimir documento NSU {}: {}", doc.getNSU(), e.getMessage());
                }
            }
        }

        controleNsu.setUltimoNsu(maxNSU);
        controleNsu.setDataUltimaConsulta(LocalDateTime.now());
        controleNsuRepository.save(controleNsu);
        logger.info("Consulta concluída, último NSU: {}", maxNSU);
    }

    private String decompressGzip(byte[] compressed) throws IOException {
        return new String(new GZIPInputStream(new ByteArrayInputStream(compressed)).readAllBytes());
    }
}
